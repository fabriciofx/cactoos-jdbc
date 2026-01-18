/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.params;

import com.github.fabriciofx.cactoos.jdbc.Param;
import com.github.fabriciofx.cactoos.jdbc.Params;
import java.io.ByteArrayOutputStream;
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
    private final Unchecked<List<Param>> parameters;

    /**
     * Ctor.
     * @param params Params
     * @param parameters Array of Param
     */
    public ParamsOf(final Params params, final Param... parameters) {
        this(
            new Joined<Param>(
                new ListOf<>(params.iterator()),
                new ListOf<>(parameters)
            )
        );
    }

    /**
     * Ctor.
     * @param parameters Array of Param
     */
    public ParamsOf(final Param... parameters) {
        this(new ListOf<>(parameters));
    }

    /**
     * Ctor.
     * @param parameters List of Param
     */
    public ParamsOf(final List<Param> parameters) {
        this.parameters = new Unchecked<>(
            new Sticky<>(
                () -> parameters
            )
        );
    }

    @Override
    public PreparedStatement prepare(
        final PreparedStatement stmt
    ) throws Exception {
        int idx = 1;
        for (final Param param : this.parameters.value()) {
            param.prepare(stmt, idx);
            ++idx;
        }
        return stmt;
    }

    @Override
    public boolean contains(final String name, final int index) {
        return this.parameters.value().get(index).name().equals(name);
    }

    @Override
    public Param param(final int index) {
        return this.parameters.value().get(index);
    }

    @Override
    public Iterator<Param> iterator() {
        return this.parameters.value().iterator();
    }

    @Override
    public byte[] asBytes() throws Exception {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        for (final Param param : this.parameters.value()) {
            stream.write(param.asBytes());
        }
        stream.flush();
        return stream.toByteArray();
    }

    @Override
    public boolean equals(final Object other) {
        return other instanceof ParamsOf
            && this.parameters.value().equals(
                ParamsOf.class.cast(other).parameters.value()
            );
    }

    @Override
    public int hashCode() {
        return this.parameters.value().hashCode();
    }
}
