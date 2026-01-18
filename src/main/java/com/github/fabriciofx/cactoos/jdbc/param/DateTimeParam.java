/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.param;

import com.github.fabriciofx.cactoos.jdbc.Param;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

    @Override
    public byte[] asBytes() throws Exception {
        final long seconds = this.datetime.toEpochSecond(ZoneOffset.UTC);
        final int nanos = this.datetime.getNano();
        return new byte[] {
            (byte) Types.TIMESTAMP,
            (byte) seconds,
            (byte) (seconds >>> 8),
            (byte) (seconds >>> 16),
            (byte) (seconds >>> 24),
            (byte) (seconds >>> 32),
            (byte) (seconds >>> 40),
            (byte) (seconds >>> 48),
            (byte) (seconds >>> 56),
            (byte) nanos,
            (byte) (nanos >>> 8),
            (byte) (nanos >>> 16),
            (byte) (nanos >>> 24),
        };
    }
}
