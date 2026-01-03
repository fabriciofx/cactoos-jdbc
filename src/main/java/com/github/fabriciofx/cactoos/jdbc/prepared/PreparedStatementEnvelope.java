/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.prepared;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * PreparedStatement Envelope.
 *
 * @since 0.4
 * @checkstyle ParameterNameCheck (1000 lines)
 * @checkstyle ParameterNumberCheck (1000 lines)
 * @checkstyle DesignForExtensionCheck (1000 lines)
 */
@SuppressWarnings(
    {
        "PMD.TooManyMethods",
        "PMD.ExcessivePublicCount",
        "PMD.CouplingBetweenObjects",
        "PMD.ReplaceJavaUtilDate",
        "PMD.ReplaceJavaUtilCalendar"
    }
)
public abstract class PreparedStatementEnvelope implements PreparedStatement {
    /**
     * The PreparedStatement.
     */
    private final PreparedStatement origin;

    /**
     * Ctor.
     * @param prepared Decorated PreparedStatement
     */
    public PreparedStatementEnvelope(final PreparedStatement prepared) {
        this.origin = prepared;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return this.origin.executeQuery();
    }

    @Override
    public int executeUpdate() throws SQLException {
        return this.origin.executeUpdate();
    }

    @Override
    public void setNull(
        final int parameterIndex,
        final int sqlType
    ) throws SQLException {
        this.origin.setNull(parameterIndex, sqlType);
    }

    @Override
    public void setBoolean(
        final int parameterIndex,
        final boolean x
    ) throws SQLException {
        this.origin.setBoolean(parameterIndex, x);
    }

    @Override
    public void setByte(
        final int parameterIndex,
        final byte x
    ) throws SQLException {
        this.origin.setByte(parameterIndex, x);
    }

    @Override
    public void setShort(
        final int parameterIndex,
        final short x
    ) throws SQLException {
        this.origin.setShort(parameterIndex, x);
    }

    @Override
    public void setInt(
        final int parameterIndex,
        final int x
    ) throws SQLException {
        this.origin.setInt(parameterIndex, x);
    }

    @Override
    public void setLong(
        final int parameterIndex,
        final long x
    ) throws SQLException {
        this.origin.setLong(parameterIndex, x);
    }

    @Override
    public void setFloat(
        final int parameterIndex,
        final float x
    ) throws SQLException {
        this.origin.setFloat(parameterIndex, x);
    }

    @Override
    public void setDouble(
        final int parameterIndex,
        final double x
    ) throws SQLException {
        this.origin.setDouble(parameterIndex, x);
    }

    @Override
    public void setBigDecimal(
        final int parameterIndex,
        final BigDecimal x
    ) throws SQLException {
        this.origin.setBigDecimal(parameterIndex, x);
    }

    @Override
    public void setString(
        final int parameterIndex,
        final String x
    ) throws SQLException {
        this.origin.setString(parameterIndex, x);
    }

    @Override
    public void setBytes(
        final int parameterIndex,
        final byte[] x
    ) throws SQLException {
        this.origin.setBytes(parameterIndex, x);
    }

    @Override
    public void setDate(
        final int parameterIndex,
        final Date x
    ) throws SQLException {
        this.origin.setDate(parameterIndex, x);
    }

    @Override
    public void setTime(
        final int parameterIndex,
        final Time x
    ) throws SQLException {
        this.origin.setTime(parameterIndex, x);
    }

    @Override
    public void setTimestamp(
        final int parameterIndex,
        final Timestamp x
    ) throws SQLException {
        this.origin.setTimestamp(parameterIndex, x);
    }

    @Override
    public void setAsciiStream(
        final int parameterIndex,
        final InputStream x,
        final int length
    ) throws SQLException {
        this.origin.setAsciiStream(parameterIndex, x, length);
    }

    @Deprecated
    @Override
    public void setUnicodeStream(
        final int parameterIndex,
        final InputStream x,
        final int length
    ) throws SQLException {
        this.origin.setUnicodeStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(
        final int parameterIndex,
        final InputStream x,
        final int length
    ) throws SQLException {
        this.origin.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void clearParameters() throws SQLException {
        this.origin.clearParameters();
    }

    @Override
    public void setObject(
        final int parameterIndex,
        final Object x,
        final int targetSqlType
    ) throws SQLException {
        this.origin.setObject(parameterIndex, x, targetSqlType);
    }

    @Override
    public void setObject(
        final int parameterIndex,
        final Object x
    ) throws SQLException {
        this.origin.setObject(parameterIndex, x);
    }

    @Override
    public boolean execute() throws SQLException {
        return this.origin.execute();
    }

    @Override
    public void addBatch() throws SQLException {
        this.origin.addBatch();
    }

    @Override
    public void setCharacterStream(
        final int parameterIndex,
        final Reader reader,
        final int length
    ) throws SQLException {
        this.origin.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setRef(
        final int parameterIndex,
        final Ref x
    ) throws SQLException {
        this.origin.setRef(parameterIndex, x);
    }

    @Override
    public void setBlob(
        final int parameterIndex,
        final Blob x
    ) throws SQLException {
        this.origin.setBlob(parameterIndex, x);
    }

    @Override
    public void setClob(
        final int parameterIndex,
        final Clob x
    ) throws SQLException {
        this.origin.setClob(parameterIndex, x);
    }

    @Override
    public void setArray(
        final int parameterIndex,
        final Array x
    ) throws SQLException {
        this.origin.setArray(parameterIndex, x);
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return this.origin.getMetaData();
    }

    @Override
    public void setDate(
        final int parameterIndex,
        final Date x,
        final Calendar cal
    ) throws SQLException {
        this.origin.setDate(parameterIndex, x, cal);
    }

    @Override
    public void setTime(
        final int parameterIndex,
        final Time x,
        final Calendar cal
    ) throws SQLException {
        this.origin.setTime(parameterIndex, x, cal);
    }

    @Override
    public void setTimestamp(
        final int parameterIndex,
        final Timestamp x,
        final Calendar cal
    ) throws SQLException {
        this.origin.setTimestamp(parameterIndex, x, cal);
    }

    @Override
    public void setNull(
        final int parameterIndex,
        final int sqlType,
        final String typeName
    ) throws SQLException {
        this.origin.setNull(parameterIndex, sqlType, typeName);
    }

    @Override
    public void setURL(
        final int parameterIndex,
        final URL x
    ) throws SQLException {
        this.origin.setURL(parameterIndex, x);
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return this.origin.getParameterMetaData();
    }

    @Override
    public void setRowId(
        final int parameterIndex,
        final RowId x
    ) throws SQLException {
        this.origin.setRowId(parameterIndex, x);
    }

    @Override
    public void setNString(
        final int parameterIndex,
        final String value
    ) throws SQLException {
        this.origin.setNString(parameterIndex, value);
    }

    @Override
    public void setNCharacterStream(
        final int parameterIndex,
        final Reader value,
        final long length
    ) throws SQLException {
        this.origin.setNCharacterStream(parameterIndex, value, length);
    }

    @Override
    public void setNClob(
        final int parameterIndex,
        final NClob value
    ) throws SQLException {
        this.origin.setNClob(parameterIndex, value);
    }

    @Override
    public void setClob(
        final int parameterIndex,
        final Reader reader,
        final long length
    ) throws SQLException {
        this.origin.setClob(parameterIndex, reader, length);
    }

    @Override
    public void setBlob(
        final int parameterIndex,
        final InputStream inputStream,
        final long length
    ) throws SQLException {
        this.origin.setBlob(parameterIndex, inputStream, length);
    }

    @Override
    public void setNClob(
        final int parameterIndex,
        final Reader reader,
        final long length
    ) throws SQLException {
        this.origin.setNClob(parameterIndex, reader, length);
    }

    @Override
    public void setSQLXML(
        final int parameterIndex,
        final SQLXML xmlObject
    ) throws SQLException {
        this.origin.setSQLXML(parameterIndex, xmlObject);
    }

    @Override
    public void setObject(
        final int parameterIndex,
        final Object x,
        final int targetSqlType,
        final int scaleOrLength
    ) throws SQLException {
        this.origin.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
    }

    @Override
    public void setAsciiStream(
        final int parameterIndex,
        final InputStream x,
        final long length
    ) throws SQLException {
        this.origin.setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(
        final int parameterIndex,
        final InputStream x,
        final long length
    ) throws SQLException {
        this.origin.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void setCharacterStream(
        final int parameterIndex,
        final Reader reader,
        final long length
    ) throws SQLException {
        this.origin.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setAsciiStream(
        final int parameterIndex,
        final InputStream x
    ) throws SQLException {
        this.origin.setAsciiStream(parameterIndex, x);
    }

    @Override
    public void setBinaryStream(
        final int parameterIndex,
        final InputStream x
    ) throws SQLException {
        this.origin.setBinaryStream(parameterIndex, x);
    }

    @Override
    public void setCharacterStream(
        final int parameterIndex,
        final Reader reader
    ) throws SQLException {
        this.origin.setCharacterStream(parameterIndex, reader);
    }

    @Override
    public void setNCharacterStream(
        final int parameterIndex,
        final Reader value
    ) throws SQLException {
        this.origin.setNCharacterStream(parameterIndex, value);
    }

    @Override
    public void setClob(
        final int parameterIndex,
        final Reader reader
    ) throws SQLException {
        this.origin.setClob(parameterIndex, reader);
    }

    @Override
    public void setBlob(
        final int parameterIndex,
        final InputStream inputStream
    ) throws SQLException {
        this.origin.setBlob(parameterIndex, inputStream);
    }

    @Override
    public void setNClob(
        final int parameterIndex,
        final Reader reader
    ) throws SQLException {
        this.origin.setNClob(parameterIndex, reader);
    }

    @Override
    public ResultSet executeQuery(final String sql) throws SQLException {
        return this.origin.executeQuery(sql);
    }

    @Override
    public int executeUpdate(final String sql) throws SQLException {
        return this.origin.executeUpdate(sql);
    }

    @Override
    public void close() throws SQLException {
        this.origin.close();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return this.origin.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(final int max) throws SQLException {
        this.origin.setMaxFieldSize(max);
    }

    @Override
    public int getMaxRows() throws SQLException {
        return this.origin.getMaxRows();
    }

    @Override
    public void setMaxRows(final int max) throws SQLException {
        this.origin.setMaxRows(max);
    }

    @Override
    public void setEscapeProcessing(final boolean enable) throws SQLException {
        this.origin.setEscapeProcessing(enable);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return this.origin.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(final int seconds) throws SQLException {
        this.origin.setQueryTimeout(seconds);
    }

    @Override
    public void cancel() throws SQLException {
        this.origin.cancel();
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
    public void setCursorName(final String name) throws SQLException {
        this.origin.setCursorName(name);
    }

    @Override
    public boolean execute(final String sql) throws SQLException {
        return this.origin.execute(sql);
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return this.origin.getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return this.origin.getUpdateCount();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return this.origin.getMoreResults();
    }

    @Override
    public void setFetchDirection(final int direction) throws SQLException {
        this.origin.setFetchSize(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return this.origin.getFetchDirection();
    }

    @Override
    public void setFetchSize(final int rows) throws SQLException {
        this.origin.setFetchSize(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return this.origin.getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return this.origin.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return this.origin.getResultSetType();
    }

    @Override
    public void addBatch(final String sql) throws SQLException {
        this.origin.addBatch(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        this.origin.clearBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return this.origin.executeBatch();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.origin.getConnection();
    }

    @Override
    public boolean getMoreResults(final int current) throws SQLException {
        return this.origin.getMoreResults();
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return this.origin.getGeneratedKeys();
    }

    @Override
    public int executeUpdate(
        final String sql,
        final int autoGeneratedKeys
    ) throws SQLException {
        return this.origin.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(
        final String sql,
        final int[] columnIndexes
    ) throws SQLException {
        return this.origin.executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(
        final String sql,
        final String[] columnNames
    ) throws SQLException {
        return this.origin.executeUpdate(sql, columnNames);
    }

    @Override
    public boolean execute(
        final String sql,
        final int autoGeneratedKeys
    ) throws SQLException {
        return this.origin.execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(
        final String sql,
        final int[] columnIndexes
    ) throws SQLException {
        return this.origin.execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(
        final String sql,
        final String[] columnNames
    ) throws SQLException {
        return this.origin.execute(sql, columnNames);
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return this.origin.getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.origin.isClosed();
    }

    @Override
    public void setPoolable(final boolean poolable) throws SQLException {
        this.origin.setPoolable(poolable);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return this.origin.isPoolable();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        this.origin.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return this.origin.isCloseOnCompletion();
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
