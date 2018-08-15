/*
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.fabriciofx.cactoos.jdbc.result;

import com.github.fabriciofx.cactoos.jdbc.Rows;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.cactoos.Scalar;

/**
 * Result as values.
 *
 * @param <T> Type of the result
 * @since 0.1
 */
public final class ResultAsValues<T> implements Scalar<List<T>> {
    /**
     * Statement that returns a Rows.
     */
    private final Statement<Rows> statement;

    /**
     * Type data to return.
     */
    private final Class<T> type;

    /**
     * Ctor.
     * @param stmt A statement that returns a Rows
     * @param tpe Type data to return
     */
    public ResultAsValues(final Statement<Rows> stmt, final Class<T> tpe) {
        this.statement = stmt;
        this.type = tpe;
    }

    @Override
    public List<T> value() throws Exception {
        final List<T> values = new LinkedList<>();
        for (final Map<String, Object> row : this.statement.result().value()) {
            for (final Object obj : row.values()) {
                values.add(this.type.cast(obj));
            }
        }
        return values;
    }
}
