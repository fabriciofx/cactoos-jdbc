/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.param;

import com.github.fabriciofx.cactoos.jdbc.Param;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.Types;

/**
 * AnyParam.
 *
 * @since 0.8.1
 */
public final class AnyParam implements Param {
    /**
     * Name.
     */
    private final String id;

    /**
     * Value.
     */
    private final Object object;

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public AnyParam(final String name, final Object value) {
        this.id = name;
        this.object = value;
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
        stmt.setObject(index, this.object);
    }

    @Override
    public byte[] asBytes() throws Exception {
        final byte[] string = this.object.toString().getBytes(
            StandardCharsets.UTF_8
        );
        final byte[] bytes = new byte[1 + string.length];
        bytes[0] = (byte) Types.JAVA_OBJECT;
        System.arraycopy(string, 0, bytes, 1, string.length);
        return bytes;
    }
}
