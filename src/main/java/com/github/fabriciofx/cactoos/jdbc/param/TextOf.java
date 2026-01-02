/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.param;

import com.github.fabriciofx.cactoos.jdbc.Param;
import java.sql.PreparedStatement;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.cactoos.Text;
import org.cactoos.text.UncheckedText;

/**
 * String param.
 *
 * @since 0.2
 */
public final class TextOf implements Param {
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
     * @param name The id
     * @param value The data
     */
    public TextOf(final String name, final String value) {
        this(new org.cactoos.text.TextOf(name), new org.cactoos.text.TextOf(value));
    }

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public TextOf(final String name, final Text value) {
        this(new org.cactoos.text.TextOf(name), value);
    }

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public TextOf(final Text name, final String value) {
        this(name, new org.cactoos.text.TextOf(value));
    }

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public TextOf(final Text name, final Text value) {
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
}
