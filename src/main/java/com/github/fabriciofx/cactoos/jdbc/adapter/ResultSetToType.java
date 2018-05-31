/**
 * The MIT License (MIT)
 *
 * Copyright (C) 2018 Fabrício Barros Cabral
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.fabriciofx.cactoos.jdbc.adapter;

import java.sql.ResultSet;
import java.util.Date;
import java.util.UUID;
import org.cactoos.Scalar;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class ResultSetToType<T> implements Scalar<T> {
    private final Scalar<ResultSet> scalar;
    private final Class<T> type;

    public ResultSetToType(final ResultSet rset, final Class<T> tpe) {
        this(() -> rset, tpe);
    }

    public ResultSetToType(final Scalar<ResultSet> sclr, final Class<T> tpe) {
        this.scalar = sclr;
        this.type = tpe;
    }

    @Override
    public T value() throws Exception {
        try (final ResultSet rset = this.scalar.value()) {
            final Object result;
            rset.next();
            try {
                if (this.type.equals(String.class)) {
                    result = rset.getString(1);
                } else if (this.type.equals(Integer.class)) {
                    result = rset.getInt(1);
                } else if (this.type.equals(Long.class)) {
                    result = rset.getLong(1);
                } else if (this.type.equals(Boolean.class)) {
                    result = rset.getBoolean(1);
                } else if (this.type.equals(Byte.class)) {
                    result = rset.getByte(1);
                } else if (this.type.equals(Date.class)) {
                    result = rset.getDate(1);
                } else if (byte[].class.equals(this.type)) {
                    result = rset.getBytes(1);
                } else if (this.type.equals(UUID.class)) {
                    result = rset.getObject(1);
                } else {
                    throw new IllegalStateException(
                        String.format("type %s is not allowed", this.type.getName())
                    );
                }
            } catch (final Exception ex) {
                throw new IllegalArgumentException(
                    String.format("Unknown type: %s", this.type),
                    ex
                );
            }
            return this.type.cast(result);
        }
    }
}
