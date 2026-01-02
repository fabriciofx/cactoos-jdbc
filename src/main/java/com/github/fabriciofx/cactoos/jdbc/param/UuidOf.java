/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.param;

import com.github.fabriciofx.cactoos.jdbc.Param;
import java.sql.PreparedStatement;
import java.util.UUID;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParserPos;

/**
 * UUID param.
 *
 * @since 0.2
 */
public final class UuidOf implements Param {
    /**
     * Name.
     */
    private final String id;

    /**
     * Value.
     */
    private final UUID uuid;

    /**
     * Ctor.
     * @param name The id
     * @param value The apply
     */
    public UuidOf(final String name, final UUID value) {
        this.id = name;
        this.uuid = value;
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
        stmt.setObject(index, this.uuid);
    }

    @Override
    public SqlNode value(final SqlParserPos from) {
        return SqlLiteral.createUuid(this.uuid, from);
    }
}
