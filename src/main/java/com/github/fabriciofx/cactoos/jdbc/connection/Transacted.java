/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018-2019 Fabricio Barros Cabral
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
package com.github.fabriciofx.cactoos.jdbc.connection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Transacted connection.
 *
 * @since 0.1
 * @checkstyle ParameterNameCheck (500 lines)
 * @checkstyle ParameterNumberCheck (500 lines)
 * @checkstyle TooManyMethods (500 lines)
 */
@SuppressWarnings(
    {
        "PMD.TooManyMethods",
        "PMD.LongVariable",
        "PMD.UseVarargs",
        "PMD.BooleanGetMethodName",
        "PMD.ExcessivePublicCount"
    }
)
public final class Transacted extends ConnectionEnvelope {
    /**
     * The connection.
     */
    private final Connection origin;

    /**
     * Number of created statements.
     */
    private final AtomicInteger stmts;

    /**
     * Ctor.
     * @param connection A Connection
     */
    public Transacted(final Connection connection) {
        super(connection);
        this.origin = connection;
        this.stmts = new AtomicInteger(0);
    }

    @Override
    public Statement createStatement() throws SQLException {
        this.stmts.getAndIncrement();
        return this.origin.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(final String sql) throws
        SQLException {
        this.stmts.getAndIncrement();
        return this.origin.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(final String sql) throws SQLException {
        this.stmts.getAndIncrement();
        return this.origin.prepareCall(sql);
    }

    @Override
    public void setAutoCommit(final boolean autoCommit) throws SQLException {
        // Don't allow change the auto commit state
    }

    @Override
    public void commit() throws SQLException {
        this.origin.commit();
        this.stmts.set(0);
    }

    @Override
    public void rollback() throws SQLException {
        this.origin.rollback();
        this.stmts.set(0);
    }

    @Override
    public void close() throws SQLException {
        if (this.stmts.get() == 0) {
            this.origin.close();
        }
    }

    @Override
    public Statement createStatement(
        final int resultSetType,
        final int resultSetConcurrency
    ) throws SQLException {
        this.stmts.getAndIncrement();
        return this.origin.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(
        final String sql,
        final int resultSetType,
        final int resultSetConcurrency
    ) throws SQLException {
        this.stmts.getAndIncrement();
        return this.origin.prepareStatement(
            sql,
            resultSetType,
            resultSetConcurrency
        );
    }

    @Override
    public CallableStatement prepareCall(
        final String sql,
        final int resultSetType,
        final int resultSetConcurrency
    ) throws SQLException {
        this.stmts.getAndIncrement();
        return this.origin.prepareCall(
            sql,
            resultSetType,
            resultSetConcurrency
        );
    }

    @Override
    public Statement createStatement(
        final int resultSetType,
        final int resultSetConcurrency,
        final int resultSetHoldability
    ) throws SQLException {
        this.stmts.getAndIncrement();
        return this.origin.createStatement(
            resultSetType,
            resultSetConcurrency,
            resultSetHoldability
        );
    }

    @Override
    public PreparedStatement prepareStatement(
        final String sql,
        final int resultSetType,
        final int resultSetConcurrency,
        final int resultSetHoldability
    ) throws SQLException {
        this.stmts.getAndIncrement();
        return this.origin.prepareStatement(
            sql,
            resultSetType,
            resultSetConcurrency,
            resultSetHoldability
        );
    }

    @Override
    public CallableStatement prepareCall(
        final String sql,
        final int resultSetType,
        final int resultSetConcurrency,
        final int resultSetHoldability
    ) throws SQLException {
        this.stmts.getAndIncrement();
        return this.origin.prepareCall(
            sql,
            resultSetType,
            resultSetConcurrency,
            resultSetHoldability
        );
    }

    @Override
    public PreparedStatement prepareStatement(
        final String sql,
        final int autoGeneratedKeys
    ) throws SQLException {
        this.stmts.getAndIncrement();
        return this.origin.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(
        final String sql,
        final int[] columnIndexes
    ) throws SQLException {
        this.stmts.getAndIncrement();
        return this.origin.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(
        final String sql,
        final String[] columnNames
    ) throws SQLException {
        this.stmts.getAndIncrement();
        return this.origin.prepareStatement(sql, columnNames);
    }
}
