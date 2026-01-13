/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Statement;
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
        "PMD.UnnecessaryLocalRule"
    }
)
public final class Transaction<T> implements Statement<T> {
    /**
     * Session.
     */
    private final Session session;

    /**
     * Callable to be executed in a transaction.
     */
    private final Callable<T> callable;

    /**
     * Ctor.
     *
     * @param session A {@link Session}
     * @param call A Callable to be executed in a transaction
     */
    public Transaction(final Session session, final Callable<T> call) {
        this.session = session;
        this.callable = call;
    }

    @Override
    public T execute() throws Exception {
        try {
            final T result = this.callable.call();
            this.session.commit();
            return result;
            // @checkstyle IllegalCatchCheck (1 line)
        } catch (final Exception ex) {
            this.session.rollback();
            throw ex;
        }
    }
}
