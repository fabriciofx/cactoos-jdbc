/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.param;

import com.github.fabriciofx.cactoos.jdbc.Param;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * DateTime param.
 *
 * @since 0.2
 */
public final class DateTimeOf implements Param {
    /**
     * Name.
     */
    private final String id;

    /**
     * Value.
     */
    private final LocalDateTime datetime;

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public DateTimeOf(final String name, final LocalDateTime value) {
        this.id = name;
        this.datetime = value;
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
        stmt.setTimestamp(index, Timestamp.valueOf(this.datetime));
    }

    @Override
    public String asString() throws Exception {
        return this.datetime.toString();
    }
}
