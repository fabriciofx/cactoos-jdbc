/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.param;

import com.github.fabriciofx.cactoos.jdbc.Param;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.time.LocalDate;

/**
 * Date param.
 *
 * @since 0.2
 */
public final class DateOf implements Param {
    /**
     * Name.
     */
    private final String id;

    /**
     * Value.
     */
    private final LocalDate date;

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public DateOf(final String name, final String value) {
        this(name, LocalDate.parse(value));
    }

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public DateOf(final String name, final LocalDate value) {
        this.id = name;
        this.date = value;
    }

    @Override
    public String name() {
        return this.id;
    }

    @Override
    public void prepare(
        final PreparedStatement stmt,
        final int index
    ) throws Exception {
        stmt.setDate(index, java.sql.Date.valueOf(this.date));
    }

    @Override
    public String asString() throws IOException {
        return this.date.toString();
    }
}
