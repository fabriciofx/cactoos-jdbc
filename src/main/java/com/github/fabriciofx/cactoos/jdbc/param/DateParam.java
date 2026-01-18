/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.param;

import com.github.fabriciofx.cactoos.jdbc.Param;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.DateString;

/**
 * DateParam.
 *
 * @since 0.2
 */
public final class DateParam implements Param {
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
    public DateParam(final String name, final String value) {
        this(name, LocalDate.parse(value));
    }

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public DateParam(final String name, final LocalDate value) {
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
    public SqlNode value(final SqlParserPos from) {
        return SqlLiteral.createDate(
            new DateString(
                this.date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            ),
            from
        );
    }

    @Override
    public byte[] asBytes() throws Exception {
        final long epoch = this.date.toEpochDay();
        return new byte[] {
            (byte) Types.DATE,
            (byte) epoch,
            (byte) (epoch >>> 8),
            (byte) (epoch >>> 16),
            (byte) (epoch >>> 24),
            (byte) (epoch >>> 32),
            (byte) (epoch >>> 40),
            (byte) (epoch >>> 48),
            (byte) (epoch >>> 56),
        };
    }
}
