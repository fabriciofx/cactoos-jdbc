/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.bytes;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import java.io.ByteArrayOutputStream;
import org.cactoos.Bytes;
import org.cactoos.bytes.BytesOf;

/**
 * QueryAsBytes.
 * <p>Convert a {@link Query} into bytes.
 * @since 0.9.0
 */
public final class QueryAsBytes implements Bytes {
    /**
     * Query.
     */
    private final Query query;

    /**
     * Ctor.
     * @param query The query
     */
    public QueryAsBytes(final Query query) {
        this.query = query;
    }

    @Override
    public byte[] asBytes() throws Exception {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(new BytesOf(this.query.sql()).asBytes());
        for (final Params params : this.query.params()) {
            stream.write(params.asBytes());
        }
        stream.flush();
        return stream.toByteArray();
    }
}
