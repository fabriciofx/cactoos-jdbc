/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.params;

import com.github.fabriciofx.cactoos.jdbc.Param;
import com.github.fabriciofx.cactoos.jdbc.Params;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.List;
import org.cactoos.list.Joined;
import org.cactoos.list.ListOf;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;

/**
 * NamedQuery Params.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.4
 */
public final class ParamsOf implements Params {
    /**
     * Params.
     */
    private final Unchecked<List<Param>> prms;

    /**
     * Ctor.
     * @param params Params
     * @param prms Array of Param
     */
    public ParamsOf(final Params params, final Param... prms) {
        this(
            new Joined<Param>(
                new ListOf<>(params.iterator()),
                new ListOf<>(prms)
            )
        );
    }

    /**
     * Ctor.
     * @param prms Array of Param
     */
    public ParamsOf(final Param... prms) {
        this(new ListOf<>(prms));
    }

    /**
     * Ctor.
     * @param prms List of Param
     */
    public ParamsOf(final List<Param> prms) {
        this.prms = new Unchecked<>(
            new Sticky<>(
                () -> prms
            )
        );
    }

    @Override
    public PreparedStatement prepare(
        final PreparedStatement stmt
    ) throws Exception {
        int idx = 1;
        for (final Param param : this.prms.value()) {
            param.prepare(stmt, idx);
            ++idx;
        }
        return stmt;
    }

    @Override
    public boolean contains(final String name, final int index) {
        return this.prms.value().get(index).name().equals(name);
    }

    @Override
    public Param param(final int index) {
        return this.prms.value().get(index);
    }

    @Override
    public Iterator<Param> iterator() {
        return this.prms.value().iterator();
    }
}
