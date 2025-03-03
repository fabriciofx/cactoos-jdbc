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
package com.github.fabriciofx.cactoos.jdbc.pagination;

import com.github.fabriciofx.cactoos.jdbc.Adapter;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.query.Counted;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import java.sql.ResultSet;
import org.cactoos.scalar.Unchecked;

/**
 * SqlPages.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @param <T> Type of the page's content
 * @since 0.8.0
 * @checkstyle ParameterNumberCheck (1500 lines)
 * @checkstyle IllegalCatchCheck (1500 lines)
 */
@SuppressWarnings(
    {
        "PMD.AvoidCatchingGenericException",
        "PMD.AvoidThrowingRawExceptionTypes"
    }
)
public final class SqlPages<T> implements Pages<T> {
    /**
     * Amount of elements in all pages.
     */
    private final Unchecked<Integer> size;

    /**
     * The adapter.
     */
    private final Adapter<T> adapter;

    /**
     * The session.
     */
    private final Session session;

    /**
     * Query tha retrieve all elements.
     */
    private final Query query;

    /**
     * The maximum amount of elements per page.
     */
    private final int limit;

    /**
     * Ctor.
     * @param session A Session
     * @param count A query that retrieve the total count of elements.
     * @param all A query tha retrieve all elements.
     * @param adapter An adapter
     * @param limit The maximum amount of elements per page.
     */
    public SqlPages(
        final Session session,
        final Query count,
        final Query all,
        final Adapter<T> adapter,
        final int limit
    ) {
        this.size = new Unchecked<>(
            () -> {
                final int result;
                try (
                    ResultSet rset = new Select(
                        session,
                        new Counted(count)
                    ).result()
                ) {
                    if (rset.next()) {
                        result = rset.getInt(1);
                    } else {
                        result = 0;
                    }
                    return result;
                } catch (final Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        );
        this.session = session;
        this.adapter = adapter;
        this.query = all;
        this.limit = limit;
    }

    @Override
    public int count() {
        final int rem = Math.min(this.size.value() % this.limit, 1);
        return this.size.value() / this.limit + rem;
    }

    @Override
    public Page<T> page(final int number) {
        return new SqlPage<>(
            this.session,
            this.adapter,
            this.query,
            this.size.value(),
            this.limit,
            number
        );
    }
}
