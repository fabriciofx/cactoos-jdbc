/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.pagination;

import com.github.fabriciofx.cactoos.jdbc.Adapter;
import com.github.fabriciofx.cactoos.jdbc.statement.Paginated;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import org.cactoos.scalar.Sticky;
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
        "PMD.AvoidThrowingRawExceptionTypes",
        "PMD.UnnecessaryLocalRule"
    }
)
public final class SqlPage<T> implements Page<T> {
    /**
     * The page's elements.
     */
    private final Unchecked<List<T>> elements;

    /**
     * Total amount of items.
     */
    private final List<Long> ttl;

    /**
     * The page's number.
     */
    private final int page;

    /**
     * Ctor.
     *
     * @param adapter An adapter
     * @param select A select statement that retrieves the elements
     * @param number The page number
     * @param size The maximum number of elements per page
     */
    public SqlPage(
        final Adapter<T> adapter,
        final Select select,
        final int number,
        final int size
    ) {
        this.ttl = new LinkedList<>();
        this.elements = new Unchecked<>(
            new Sticky<>(
                () -> {
                    try (
                        ResultSet rset = new Paginated(
                            select,
                            number,
                            size
                        ).execute()
                    ) {
                        final List<T> list = new LinkedList<>();
                        while (rset.next()) {
                            this.ttl.add(rset.getLong("__total__"));
                            list.add(adapter.adapt(rset));
                        }
                        return list;
                    } catch (final Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            )
        );
        this.page = number;
    }

    @Override
    public List<T> items() {
        return this.elements.value();
    }

    @Override
    public long total() {
        return this.ttl.get(0);
    }

    @Override
    public int number() {
        return this.page;
    }

    @Override
    public int size() {
        return this.elements.value().size();
    }
}
