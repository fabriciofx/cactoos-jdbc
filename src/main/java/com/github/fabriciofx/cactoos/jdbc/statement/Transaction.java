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
@SuppressWarnings({"PMD.AvoidCatchingGenericException", "PMD.CloseResource"})
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
     * @param sssn A session
     * @param call A Callable to be executed in a transaction
     */
    public Transaction(final Session sssn, final Callable<T> call) {
        this(new Transacted(sssn), call);
    }

    /**
     * Ctor.
     * @param sssn A transacted session
     * @param call A Callable to be executed in a transaction
     */
    public Transaction(final Transacted sssn, final Callable<T> call) {
        this.session = sssn;
        this.callable = call;
    }

    @Override
    public T result() throws Exception {
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
