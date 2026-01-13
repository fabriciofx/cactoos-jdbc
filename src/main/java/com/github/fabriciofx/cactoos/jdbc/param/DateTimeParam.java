/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.param;

import com.github.fabriciofx.cactoos.jdbc.Param;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParserPos;

/**
 * DateTimeParam.
 *
 * @since 0.2
 */
public final class DateTimeParam implements Param {
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
    public DateTimeParam(final String name, final LocalDateTime value) {
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
    public SqlNode value(final SqlParserPos from) {
        return null;
    }
}
