/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import com.github.fabriciofx.cactoos.jdbc.session.Transacted;
import java.sql.Connection;
import java.util.concurrent.Callable;

/**
 * StatementTransaction.
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
    private final Session session;

    /**
     * Callable to be executed in a transaction.
     */
    private final Callable<T> callable;

    /**
     * Ctor.
     *
     * @param sssn A session
     * @param call A Callable to be executed in a transaction
     */
    public Transaction(final Session sssn, final Callable<T> call) {
        this(new Transacted(sssn), call);
    }

    /**
     * Ctor.
     *
     * @param sssn A transacted session
     * @param call A Callable to be executed in a transaction
     */
    public Transaction(final Transacted sssn, final Callable<T> call) {
        this.session = sssn;
        this.callable = call;
    }

    @Override
    public T execute() throws Exception {
        final Connection connection = this.session.connection();
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
}
