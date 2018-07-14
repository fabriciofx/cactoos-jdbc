/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018 Fabrício Barros Cabral
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.fabriciofx.cactoos.jdbc.txn;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

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
public final class TransactedConnection implements Connection {
    /**
     * The connection.
     */
    private final Connection origin;

    /**
     * Ctor.
     * @param connection A Connection
     */
    public TransactedConnection(final Connection connection) {
        this.origin = connection;
    }

    @Override
    public Statement createStatement() throws SQLException {
        return this.origin.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(final String sql) throws
        SQLException {
        return this.origin.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(final String sql) throws SQLException {
        return this.origin.prepareCall(sql);
    }

    @Override
    public String nativeSQL(final String sql) throws SQLException {
        return this.origin.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(final boolean autoCommit) throws SQLException {
        this.origin.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return this.origin.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        this.origin.commit();
        if (!this.origin.isClosed()) {
            this.origin.close();
        }
    }

    @Override
    public void rollback() throws SQLException {
        this.origin.rollback();
        if (!this.origin.isClosed()) {
            this.origin.close();
        }
    }

    @Override
    public void close() throws SQLException {
        // Does nothing
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.origin.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return this.origin.getMetaData();
    }

    @Override
    public void setReadOnly(final boolean readOnly) throws SQLException {
        this.origin.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return this.origin.isReadOnly();
    }

    @Override
    public void setCatalog(final String catalog) throws SQLException {
        this.origin.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return this.origin.getCatalog();
    }

    @Override
    public void setTransactionIsolation(final int level) throws SQLException {
        this.origin.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return this.origin.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return this.origin.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        this.origin.clearWarnings();
    }

    @Override
    public Statement createStatement(
        final int resultSetType,
        final int resultSetConcurrency
    ) throws SQLException {
        return this.origin.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(
        final String sql,
        final int resultSetType,
        final int resultSetConcurrency
    ) throws SQLException {
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
        return this.origin.prepareCall(
            sql,
            resultSetType,
            resultSetConcurrency
        );
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return this.origin.getTypeMap();
    }

    @Override
    public void setTypeMap(final Map<String, Class<?>> map) throws
        SQLException {
        this.origin.setTypeMap(map);
    }

    @Override
    public void setHoldability(final int holdability) throws SQLException {
        this.origin.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return this.origin.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return this.origin.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(final String name) throws SQLException {
        return this.origin.setSavepoint(name);
    }

    @Override
    public void rollback(final Savepoint savepoint) throws SQLException {
        this.origin.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(
        final Savepoint savepoint
    ) throws SQLException {
        this.origin.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(
        final int resultSetType,
        final int resultSetConcurrency,
        final int resultSetHoldability
    ) throws SQLException {
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
        return this.origin.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(
        final String sql,
        final int[] columnIndexes
    ) throws SQLException {
        return this.origin.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(
        final String sql,
        final String[] columnNames
    ) throws SQLException {
        return this.origin.prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return this.origin.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return this.origin.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return this.origin.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return this.origin.createSQLXML();
    }

    @Override
    public boolean isValid(final int timeout) throws SQLException {
        return this.origin.isValid(timeout);
    }

    @Override
    public void setClientInfo(
        final String name,
        final String value
    ) throws SQLClientInfoException {
        this.origin.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(
        final Properties properties
    ) throws SQLClientInfoException {
        this.origin.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(final String name) throws SQLException {
        return this.origin.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return this.origin.getClientInfo();
    }

    @Override
    public Array createArrayOf(
        final String typeName,
        final Object[] elements
    ) throws SQLException {
        return this.origin.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(
        final String typeName,
        final Object[] attributes
    ) throws SQLException {
        return this.origin.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(final String schema) throws SQLException {
        this.origin.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return this.origin.getSchema();
    }

    @Override
    public void abort(final Executor executor) throws SQLException {
        this.origin.abort(executor);
    }

    @Override
    public void setNetworkTimeout(
        final Executor executor,
        final int milliseconds
    ) throws SQLException {
        this.origin.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return this.origin.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        return this.origin.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return this.origin.isWrapperFor(iface);
    }
}
