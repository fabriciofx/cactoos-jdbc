/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.param;

import com.github.fabriciofx.cactoos.jdbc.Param;
import java.sql.PreparedStatement;
import java.sql.Types;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.cactoos.Text;
import org.cactoos.bytes.BytesOf;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;

/**
 * TextParam.
 *
 * @since 0.2
 */
public final class TextParam implements Param {
    /**
     * Name.
     */
    private final Text id;

    /**
     * Value.
     */
    private final Text text;

    /**
     * Ctor.
     *
     * @param name The id
     * @param value The data
     */
    public TextParam(final String name, final String value) {
        this(new TextOf(name), new TextOf(value));
    }

    /**
     * Ctor.
     *
     * @param name The id
     * @param value The data
     */
    public TextParam(final String name, final Text value) {
        this(new TextOf(name), value);
    }

    /**
     * Ctor.
     *
     * @param name The id
     * @param value The data
     */
    public TextParam(final Text name, final String value) {
        this(name, new TextOf(value));
    }

    /**
     * Ctor.
     *
     * @param name The id
     * @param value The data
     */
    public TextParam(final Text name, final Text value) {
        this.id = name;
        this.text = value;
    }

    @Override
    public String name() {
        return new UncheckedText(this.id).asString();
    }

    @Override
    public void prepare(
        final PreparedStatement stmt,
        final int index
    ) throws Exception {
        stmt.setString(index, this.text.asString());
    }

    @Override
    public SqlNode value(final SqlParserPos from) {
        return SqlLiteral.createCharString(this.text.toString(), from);
    }

    @Override
    public byte[] asBytes() throws Exception {
        final byte[] txt = new BytesOf(this.text).asBytes();
        final byte[] bytes = new byte[1 + txt.length];
        bytes[0] = (byte) Types.VARCHAR;
        System.arraycopy(txt, 0, bytes, 1, txt.length);
        return bytes;
    }
}
