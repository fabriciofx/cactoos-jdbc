/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018-2025 Fabrício Barros Cabral
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
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

import com.github.fabriciofx.cactoos.jdbc.Statement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import org.cactoos.Scalar;

/**
 * Result as data.
 *
 * @param <T> Type of the result
 * @since 0.1
 */
public final class ResultSetAsValue<T> implements Scalar<T> {
    /**
     * Statement that returns a ResultSet.
     */
    private final Statement<ResultSet> statement;

    /**
     * Ctor.
     * @param stmt A statement
     */
    public ResultSetAsValue(final Statement<ResultSet> stmt) {
        this.statement = stmt;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T value() throws Exception {
        try (ResultSet rset = this.statement.result()) {
            final Iterator<Map<String, Object>> iter = new ResultSetAsRows(rset)
                .iterator();
            if (iter.hasNext()) {
                return (T) iter.next().values().toArray()[0];
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
