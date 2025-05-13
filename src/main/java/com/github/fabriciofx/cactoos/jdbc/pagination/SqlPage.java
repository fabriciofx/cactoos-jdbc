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
import com.github.fabriciofx.cactoos.jdbc.query.Paginated;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import org.cactoos.scalar.Unchecked;

/**
 * SqlPage.
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
public final class SqlPage<T> implements Page<T> {
    /**
     * The page's elements.
     */
    private final Unchecked<List<T>> items;

    /**
     * The session.
     */
    private final Session session;

    /**
     * The adapter.
     */
    private final Adapter<T> adapter;

    /**
     * Query that returns all objects.
     */
    private final Query all;

    /**
     * Total of all pages elements.
     */
    private final int size;

    /**
     * Number of elements by page.
     */
    private final int limit;

    /**
     * The page's number.
     */
    private final int number;

    /**
     * Ctor.
     * @param session A session
     * @param adapter An adapter
     * @param all A query that retrieves all elements
     * @param size Number of elements in this page
     * @param limit The maximum number of elements per page
     * @param number The page number
     */
    public SqlPage(
        final Session session,
        final Adapter<T> adapter,
        final Query all,
        final int size,
        final int limit,
        final int number
    ) {
        this.items = new Unchecked<>(
            () -> {
                try (
                    ResultSet rset = new Select(
                        session,
                        new Paginated(all, limit, number)
                    ).result()
                ) {
                    final List<T> elements = new LinkedList<>();
                    while (rset.next()) {
                        elements.add(adapter.adapt(rset));
                    }
                    return elements;
                } catch (final Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        );
        this.session = session;
        this.all = all;
        this.adapter = adapter;
        this.size = size;
        this.limit = limit;
        this.number = number;
    }

    @Override
    public List<T> content() {
        return this.items.value();
    }

    @Override
    public boolean hasNext() {
        final int rem = Math.min(this.size % this.limit, 1);
        final int pages = this.size / this.limit + rem;
        return this.number < pages - 1;
    }

    @Override
    public Page<T> next() {
        return new SqlPage<>(
            this.session,
            this.adapter,
            this.all,
            this.size,
            this.limit,
            this.number + 1
        );
    }

    @Override
    public boolean hasPrevious() {
        return this.number > 0;
    }

    @Override
    public Page<T> previous() {
        return new SqlPage<>(
            this.session,
            this.adapter,
            this.all,
            this.size,
            this.limit,
            this.number - 1
        );
    }
}
