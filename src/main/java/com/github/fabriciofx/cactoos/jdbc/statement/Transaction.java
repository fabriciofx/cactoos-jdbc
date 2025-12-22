/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
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
     * The connection.
     */
    private final Connection connexio;

    /**
     * Callable to be executed in a transaction.
     */
    private final Callable<T> callable;

    /**
     * Ctor.
     *
     * @param connection A session
     * @param call A Callable to be executed in a transaction
     */
    public Transaction(final Connection connection, final Callable<T> call) {
        this.connexio = connection;
        this.callable = call;
    }

    @Override
    public T execute() throws Exception {
        try {
            final T res = this.callable.call();
            this.connexio.commit();
            return res;
            // @checkstyle IllegalCatchCheck (1 line)
        } catch (final Exception ex) {
            this.connexio.rollback();
            throw ex;
        }
    }

    @Override
    public Connection connection() {
        return this.connexio;
    }

    @Override
    public Query query() {
        return new QueryOf("");
    }
}
