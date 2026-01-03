/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.param;

import com.github.fabriciofx.cactoos.jdbc.Param;
import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.util.UUID;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParserPos;

/**
 * UuidAsBytesParam.
 *
 * @since 0.2
 */
public final class UuidAsBytesParam implements Param {
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
    public UuidAsBytesParam(final String name, final UUID value) {
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
        // @checkstyle MagicNumber (1 line)
        final ByteBuffer bytes = ByteBuffer.wrap(new byte[16]);
        bytes.putLong(this.uuid.getMostSignificantBits());
        bytes.putLong(this.uuid.getLeastSignificantBits());
        stmt.setBytes(index, bytes.array());
    }

    @Override
    public SqlNode value(final SqlParserPos from) {
        return null;
    }
}
