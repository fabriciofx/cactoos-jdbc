/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.session.Transacted;
import java.sql.Connection;
import java.util.concurrent.Callable;

/**
 * Transaction statement.
 *
 * @param <T> Type of the result
 * @since 0.1
 */
@SuppressWarnings(
    {
        "PMD.AvoidCatchingGenericException",
        "PMD.UnnecessaryLocalRule",
        "PMD.CloseResource"
    }
)
public final class Transaction<T> implements Statement<T> {
    /**
     * The session.
     */
    private final Session sssn;

    /**
     * Callable to be executed in a transaction.
     */
    private final Callable<T> callable;

    /**
     * Ctor.
     *
     * @param session A session
     * @param call A Callable to be executed in a transaction
     */
    public Transaction(final Session session, final Callable<T> call) {
        this(new Transacted(session), call);
    }

    /**
     * Ctor.
     *
     * @param session A transacted session
     * @param call A Callable to be executed in a transaction
     */
    public Transaction(final Transacted session, final Callable<T> call) {
        this.sssn = session;
        this.callable = call;
    }

    @Override
    public T execute() throws Exception {
        final Connection connection = this.sssn.connection();
        try {
            final T res = this.callable.call();
            connection.commit();
            return res;
            // @checkstyle IllegalCatchCheck (1 line)
        } catch (final Exception ex) {
            connection.rollback();
            throw ex;
        }
    }

    @Override
    public Session session() {
        return this.sssn;
    }

    @Override
    public Query query() {
        return new QueryOf("");
    }
}
