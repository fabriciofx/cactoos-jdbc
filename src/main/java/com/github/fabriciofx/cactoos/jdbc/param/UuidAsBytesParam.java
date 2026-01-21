/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.param;

import com.github.fabriciofx.cactoos.jdbc.Param;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.UUID;

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
     *
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
        final long most = this.uuid.getMostSignificantBits();
        final long least = this.uuid.getLeastSignificantBits();
        stmt.setBytes(
            index,
            new byte[]{
                (byte) (most >>> 56),
                (byte) (most >>> 48),
                (byte) (most >>> 40),
                (byte) (most >>> 32),
                (byte) (most >>> 24),
                (byte) (most >>> 16),
                (byte) (most >>> 8),
                (byte) most,
                (byte) (least >>> 56),
                (byte) (least >>> 48),
                (byte) (least >>> 40),
                (byte) (least >>> 32),
                (byte) (least >>> 24),
                (byte) (least >>> 16),
                (byte) (least >>> 8),
                (byte) least,
            }
        );
    }

    @Override
    public byte[] asBytes() throws Exception {
        final long most = this.uuid.getMostSignificantBits();
        final long least = this.uuid.getLeastSignificantBits();
        return new byte[]{
            (byte) Types.OTHER,
            (byte) most,
            (byte) (most >>> 8),
            (byte) (most >>> 16),
            (byte) (most >>> 24),
            (byte) (most >>> 32),
            (byte) (most >>> 40),
            (byte) (most >>> 48),
            (byte) (most >>> 56),
            (byte) least,
            (byte) (least >>> 8),
            (byte) (least >>> 16),
            (byte) (least >>> 24),
            (byte) (least >>> 32),
            (byte) (least >>> 40),
            (byte) (least >>> 48),
            (byte) (least >>> 56),
        };
    }
}
