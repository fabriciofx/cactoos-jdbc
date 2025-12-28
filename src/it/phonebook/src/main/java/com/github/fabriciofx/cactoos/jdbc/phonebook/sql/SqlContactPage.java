/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.Adapter;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.pagination.Page;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.query.Paginated;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;

/**
 * SqlContactPage.
 *
 * <p>There is no thread-safety guarantee.
 *
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
public final class SqlContactPage implements Page<Contact> {
    /**
     * The page's elements.
     */
    private final Unchecked<List<Contact>> elements;

    /**
     * Total amount of items.
     */
    private final List<Long> amount;

    /**
     * The page's number.
     */
    private final int page;

    /**
     * Ctor.
     *
     * @param session A session
     * @param adapter An adapter
     * @param number The page number
     * @param size The maximum number of elements per page
     */
    public SqlContactPage(
        final Session session,
        final Adapter<Contact> adapter,
        final int number,
        final int size
    ) {
        this.amount = new ArrayList<>(0);
        this.elements = new Unchecked<>(
            new Sticky<>(
                () -> {
                    try (
                        Connection connection = session.connection();
                        ResultSet rset = new Select(
                            connection,
                            new Paginated(
                                new QueryOf("SELECT id FROM contact"),
                                number,
                                size
                            )
                        ).execute()
                    ) {
                        final List<Contact> list = new LinkedList<>();
                        if (rset.next()) {
                            this.amount.add(rset.getLong("__total__"));
                            list.add(adapter.adapt(rset));
                        }
                        while (rset.next()) {
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
    public List<Contact> items() {
        return this.elements.value();
    }

    @Override
    public long total() {
        final long num;
        this.elements.value();
        if (this.amount.isEmpty()) {
            num = 0;
        } else {
            num = this.amount.get(0);
        }
        return num;
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
