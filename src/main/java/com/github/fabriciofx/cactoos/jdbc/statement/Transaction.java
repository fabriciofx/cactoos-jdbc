/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
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
    private final Connexio connexio;

    /**
     * Callable to be executed in a transaction.
     */
    private final Callable<T> callable;

    /**
     * Ctor.
     *
     * @param connexio A session
     * @param call A Callable to be executed in a transaction
     */
    public Transaction(final Connexio connexio, final Callable<T> call) {
        this.connexio = connexio;
        this.callable = call;
    }

    @Override
    public T execute() throws Exception {
        try {
            final T result = this.callable.call();
            this.connexio.commit();
            return result;
            // @checkstyle IllegalCatchCheck (1 line)
        } catch (final Exception ex) {
            this.connexio.rollback();
            throw ex;
        }
    }

    @Override
    public Query query() {
        return new QueryOf("");
    }
}
