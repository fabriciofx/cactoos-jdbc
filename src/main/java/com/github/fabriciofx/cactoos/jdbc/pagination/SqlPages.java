/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
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
        "PMD.AvoidThrowingRawExceptionTypes",
        "PMD.UnnecessaryLocalRule"
    }
)
public final class SqlPages<T> implements Pages<T> {
    /**
     * Number of elements in all pages.
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
     * Query that retrieve all elements.
     */
    private final Query query;

    /**
     * The maximum number of elements per page.
     */
    private final int limit;

    /**
     * Ctor.
     * @param session A Session
     * @param count A query that retrieves the total count of elements.
     * @param all A query that retrieves all elements.
     * @param adapter An adapter
     * @param limit The maximum number of elements per page.
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
                    ).execute()
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
