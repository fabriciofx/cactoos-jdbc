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
package com.github.fabriciofx.cactoos.jdbc.log;

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
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cactoos.text.FormattedText;
import org.cactoos.text.UncheckedText;

/**
 * Logged PreparedStatement.
 *
 * @since 0.1
 * @checkstyle ParameterNameCheck (1500 lines)
 * @checkstyle ParameterNumberCheck (1500 lines)
 * @checkstyle LineLengthCheck (1500 lines)
 */
@SuppressWarnings(
    {
        "PMD.TooManyMethods",
        "PMD.LongVariable",
        "PMD.UseVarargs",
        "PMD.LoggerIsNotStaticFinal",
        "PMD.BooleanGetMethodName",
        "PMD.ExcessivePublicCount",
        "PMD.AvoidDuplicateLiterals",
        "PMD.AvoidUsingShortType"
    }
)
public final class LoggedPreparedStatement implements PreparedStatement {
    /**
     * The PreparedStatement.
     */
    private final PreparedStatement origin;

    /**
     * The name of source data.
     */
    private final String source;

    /**
     * The logger.
     */
    private final Logger logger;

    /**
     * The log level.
     */
    private final Level level;

    /**
     * The PreparedStatement id.
     */
    private final int id;

    /**
     * Ctor.
     * @param stmt Decorated PreparedStatement
     * @param src The name of source data
     * @param lggr The logger
     * @param lvl The log level
     * @param id The PreparedStatement id
     */
    public LoggedPreparedStatement(
        final PreparedStatement stmt,
        final String src,
        final Logger lggr,
        final Level lvl,
        final int id
    ) {
        this.origin = stmt;
        this.source = src;
        this.logger = lggr;
        this.level = lvl;
        this.id = id;
    }
    @Override
    public ResultSet executeQuery() throws SQLException {
        final Instant start = Instant.now();
        final ResultSet rset = this.origin.executeQuery();
        final Instant end = Instant.now();
        final long millis = Duration.between(start, end).toMillis();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] retrieved a ResultSet in %dms.",
                    this.source,
                    this.id,
                    millis
                )
            ).asString()
        );
        return rset;
    }
    @Override
    public int executeUpdate() throws SQLException {
        final Instant start = Instant.now();
        final int updated = this.origin.executeUpdate();
        final Instant end = Instant.now();
        final long millis = Duration.between(start, end).toMillis();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] updated a source and returned '%d' in %dms.",
                    this.source,
                    this.id,
                    updated,
                    millis
                )
            ).asString()
        );
        return updated;
    }
    @Override
    public void setNull(final int parameterIndex, final int sqlType) throws SQLException {
        this.origin.setNull(parameterIndex, sqlType);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%d' value.",
                    this.source,
                    this.id,
                    parameterIndex,
                    sqlType
                )
            ).asString()
        );
    }
    @Override
    public void setBoolean(final int parameterIndex, final boolean x) throws SQLException {
        this.origin.setBoolean(parameterIndex, x);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%s' value.",
                    this.source,
                    this.id,
                    parameterIndex,
                    x
                )
            ).asString()
        );
    }
    @Override
    public void setByte(final int parameterIndex, final byte x) throws SQLException {
        this.origin.setByte(parameterIndex, x);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%d' value.",
                    this.source,
                    this.id,
                    parameterIndex,
                    x
                )
            ).asString()
        );
    }
    @Override
    public void setShort(final int parameterIndex, final short x) throws SQLException {
        this.origin.setShort(parameterIndex, x);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%d' value.",
                    this.source,
                    this.id,
                    parameterIndex,
                    x
                )
            ).asString()
        );
    }
    @Override
    public void setInt(final int parameterIndex, final int x) throws SQLException {
        this.origin.setInt(parameterIndex, x);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%d' value.",
                    this.source,
                    this.id,
                    parameterIndex,
                    x
                )
            ).asString()
        );
    }
    @Override
    public void setLong(final int parameterIndex, final long x) throws SQLException {
        this.origin.setLong(parameterIndex, x);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%d' value.",
                    this.source,
                    this.id,
                    parameterIndex,
                    x
                )
            ).asString()
        );
    }
    @Override
    public void setFloat(final int parameterIndex, final float x) throws SQLException {
        this.origin.setFloat(parameterIndex, x);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%f' value.",
                    this.source,
                    this.id,
                    parameterIndex,
                    x
                )
            ).asString()
        );
    }
    @Override
    public void setDouble(final int parameterIndex, final double x) throws SQLException {
        this.origin.setDouble(parameterIndex, x);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%f' value.",
                    this.source,
                    this.id,
                    parameterIndex,
                    x
                )
            ).asString()
        );
    }
    @Override
    public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {
        this.origin.setBigDecimal(parameterIndex, x);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%s' value.",
                    this.source,
                    this.id,
                    parameterIndex,
                    x.toString()
                )
            ).asString()
        );
    }
    @Override
    public void setString(final int parameterIndex, final String x) throws SQLException {
        this.origin.setString(parameterIndex, x);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%s' value.",
                    this.source,
                    this.id,
                    parameterIndex,
                    x
                )
            ).asString()
        );
    }
    @Override
    public void setBytes(final int parameterIndex, final byte[] x) throws SQLException {
        this.origin.setBytes(parameterIndex, x);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%d' bytes.",
                    this.source,
                    this.id,
                    parameterIndex,
                    x.length
                )
            ).asString()
        );
    }
    @Override
    public void setDate(final int parameterIndex, final Date x) throws SQLException {
        this.origin.setDate(parameterIndex, x);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%s' value.",
                    this.source,
                    this.id,
                    parameterIndex,
                    x.toString()
                )
            ).asString()
        );
    }
    @Override
    public void setTime(final int parameterIndex, final Time x) throws SQLException {
        this.origin.setTime(parameterIndex, x);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%s' value.",
                    this.source,
                    this.id,
                    parameterIndex,
                    x.toString()
                )
            ).asString()
        );
    }
    @Override
    public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {
        this.origin.setTimestamp(parameterIndex, x);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%s' value.",
                    this.source,
                    this.id,
                    parameterIndex,
                    x.toString()
                )
            ).asString()
        );
    }
    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        this.origin.setAsciiStream(parameterIndex, x, length);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%d' bytes.",
                    this.source,
                    this.id,
                    parameterIndex,
                    length
                )
            ).asString()
        );
    }
    /**
     * Set a stream to Unicode.
     * @deprecated It not should be used
     * @param parameterIndex Parameter parameterIndex
     * @param x InputStream
     * @param length Data length
     * @throws SQLException If fails
     */
    @Deprecated
    public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        this.origin.setUnicodeStream(parameterIndex, x, length);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%d' bytes.",
                    this.source,
                    this.id,
                    parameterIndex,
                    length
                )
            ).asString()
        );
    }
    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        this.origin.setBinaryStream(parameterIndex, x, length);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%d' bytes.",
                    this.source,
                    this.id,
                    parameterIndex,
                    length
                )
            ).asString()
        );
    }
    @Override
    public void clearParameters() throws SQLException {
        this.origin.clearParameters();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] parameters has been cleaned.",
                    this.source,
                    this.id
                )
            ).asString()
        );
    }
    @Override
    public void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
        this.origin.setObject(parameterIndex, x, targetSqlType);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%s' value and '%d' type.",
                    this.source,
                    this.id,
                    parameterIndex,
                    x.toString(),
                    targetSqlType
                )
            ).asString()
        );
    }
    @Override
    public void setObject(final int parameterIndex, final Object x) throws SQLException {
        this.origin.setObject(parameterIndex, x);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%s' value.",
                    this.source,
                    this.id,
                    parameterIndex,
                    x.toString()
                )
            ).asString()
        );
    }
    @Override
    public boolean execute() throws SQLException {
        final Instant start = Instant.now();
        final boolean result = this.origin.execute();
        final Instant end = Instant.now();
        final long millis = Duration.between(start, end).toMillis();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] returned '%s' in %dms.",
                    this.source,
                    this.id,
                    result,
                    millis
                )
            ).asString()
        );
        return result;
    }
    @Override
    public void addBatch() throws SQLException {
        this.origin.addBatch();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] added a batch.",
                    this.source,
                    this.id
                )
            ).asString()
        );
    }
    @Override
    public void setCharacterStream(final int parameterIndex, final Reader reader, final int length) throws SQLException {
        this.origin.setCharacterStream(parameterIndex, reader, length);
    }
    @Override
    public void setRef(final int parameterIndex, final Ref x) throws SQLException {
        this.origin.setRef(parameterIndex, x);
    }
    @Override
    public void setBlob(final int parameterIndex, final Blob x) throws SQLException {
        this.origin.setBlob(parameterIndex, x);
    }
    @Override
    public void setClob(final int parameterIndex, final Clob x) throws SQLException {
        this.origin.setClob(parameterIndex, x);
    }
    @Override
    public void setArray(final int parameterIndex, final Array x) throws SQLException {
        this.origin.setArray(parameterIndex, x);
    }
    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return this.origin.getMetaData();
    }
    @Override
    public void setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
        this.origin.setDate(parameterIndex, x, cal);
    }
    @Override
    public void setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
        this.origin.setTime(parameterIndex, x, cal);
    }
    @Override
    public void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
        this.origin.setTimestamp(parameterIndex, x, cal);
    }
    @Override
    public void setNull(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
        this.origin.setNull(parameterIndex, sqlType, typeName);
    }
    @Override
    public void setURL(final int parameterIndex, final URL x) throws SQLException {
        this.origin.setURL(parameterIndex, x);
    }
    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return this.origin.getParameterMetaData();
    }
    @Override
    public void setRowId(final int parameterIndex, final RowId x) throws SQLException {
        this.origin.setRowId(parameterIndex, x);
    }
    @Override
    public void setNString(final int parameterIndex, final String value) throws SQLException {
        this.origin.setNString(parameterIndex, value);
    }
    @Override
    public void setNCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException {
        this.origin.setNCharacterStream(parameterIndex, value, length);
    }
    @Override
    public void setNClob(final int parameterIndex, final NClob value) throws SQLException {
        this.origin.setNClob(parameterIndex, value);
    }
    @Override
    public void setClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
        this.origin.setClob(parameterIndex, reader, length);
    }
    @Override
    public void setBlob(final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {
        this.origin.setBlob(parameterIndex, inputStream, length);
    }
    @Override
    public void setNClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
        this.origin.setNClob(parameterIndex, reader, length);
    }
    @Override
    public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException {
        this.origin.setSQLXML(parameterIndex, xmlObject);
    }
    @Override
    public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scaleOrLength) throws SQLException {
        this.origin.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
    }
    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
        this.origin.setAsciiStream(parameterIndex, x, length);
    }
    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
        this.origin.setBinaryStream(parameterIndex, x, length);
    }
    @Override
    public void setCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException {
        this.origin.setCharacterStream(parameterIndex, reader, length);
    }
    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException {
        this.origin.setAsciiStream(parameterIndex, x);
    }
    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException {
        this.origin.setBinaryStream(parameterIndex, x);
    }
    @Override
    public void setCharacterStream(final int parameterIndex, final Reader reader) throws SQLException {
        this.origin.setCharacterStream(parameterIndex, reader);
    }
    @Override
    public void setNCharacterStream(final int parameterIndex, final Reader value) throws SQLException {
        this.origin.setNCharacterStream(parameterIndex, value);
    }
    @Override
    public void setClob(final int parameterIndex, final Reader reader) throws SQLException {
        this.origin.setClob(parameterIndex, reader);
    }
    @Override
    public void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException {
        this.origin.setBlob(parameterIndex, inputStream);
    }
    @Override
    public void setNClob(final int parameterIndex, final Reader reader) throws SQLException {
        this.origin.setNClob(parameterIndex, reader);
    }
    @Override
    public ResultSet executeQuery(final String sql) throws SQLException {
        final Instant start = Instant.now();
        final ResultSet rset = this.origin.executeQuery(sql);
        final Instant end = Instant.now();
        final long millis = Duration.between(start, end).toMillis();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] executed SQL %s in %dms.",
                    this.source,
                    this.id,
                    sql,
                    millis
                )
            ).asString()
        );
        return rset;
    }
    @Override
    public int executeUpdate(final String sql) throws SQLException {
        final Instant start = Instant.now();
        final int updated = this.origin.executeUpdate(sql);
        final Instant end = Instant.now();
        final long millis = Duration.between(start, end).toMillis();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] executed SQL %s in %dms.",
                    this.source,
                    this.id,
                    sql,
                    updated,
                    millis
                )
            ).asString()
        );
        return updated;
    }
    @Override
    public void close() throws SQLException {
        this.origin.close();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] closed.",
                    this.source,
                    this.id
                )
            ).asString()
        );
    }
    @Override
    public int getMaxFieldSize() throws SQLException {
        return this.origin.getMaxFieldSize();
    }
    @Override
    public void setMaxFieldSize(final int max) throws SQLException {
        this.origin.setMaxFieldSize(max);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed max field size to '%d' bytes.",
                    this.source,
                    this.id,
                    max
                )
            ).asString()
        );
    }
    @Override
    public int getMaxRows() throws SQLException {
        return this.origin.getMaxRows();
    }
    @Override
    public void setMaxRows(final int max) throws SQLException {
        this.origin.setMaxRows(max);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed max rows to '%d'.",
                    this.source,
                    this.id,
                    max
                )
            ).asString()
        );
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
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed timeout to '%d' seconds.",
                    this.source,
                    this.id,
                    seconds
                )
            ).asString()
        );
    }
    @Override
    public void cancel() throws SQLException {
        this.origin.cancel();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] canceled.",
                    this.source,
                    this.id
                )
            ).asString()
        );
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
        final Instant start = Instant.now();
        final boolean result = this.origin.execute(sql);
        final Instant end = Instant.now();
        final long millis = Duration.between(start, end).toMillis();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] executed SQL '%s' in %dms.",
                    this.source,
                    this.id,
                    sql,
                    millis
                )
            ).asString()
        );
        return result;
    }
    @Override
    public ResultSet getResultSet() throws SQLException {
        final Instant start = Instant.now();
        final ResultSet rset = this.origin.getResultSet();
        final Instant end = Instant.now();
        final long millis = Duration.between(start, end).toMillis();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] returned a ResultSet in %dms.",
                    this.source,
                    this.id,
                    millis
                )
            ).asString()
        );
        return rset;
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
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] added batch with SQL '%s'.",
                    this.source,
                    this.id,
                    sql
                )
            ).asString()
        );
    }
    @Override
    public void clearBatch() throws SQLException {
        this.origin.clearBatch();
    }
    @Override
    public int[] executeBatch() throws SQLException {
        final int[] counts = this.origin.executeBatch();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] returned '%d' counts.",
                    this.source,
                    this.id,
                    counts.length
                )
            ).asString()
        );
        return counts;
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
        final Instant start = Instant.now();
        final ResultSet rset = this.origin.getGeneratedKeys();
        final Instant end = Instant.now();
        final long millis = Duration.between(start, end).toMillis();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] returned a ResultSet keys in %dms.",
                    this.source,
                    this.id,
                    millis
                )
            ).asString()
        );
        return rset;
    }
    @Override
    public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
        return this.origin.executeUpdate(sql, autoGeneratedKeys);
    }
    @Override
    public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
        return this.origin.executeUpdate(sql, columnIndexes);
    }
    @Override
    public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
        return this.origin.executeUpdate(sql, columnNames);
    }
    @Override
    public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
        return this.origin.execute(sql, autoGeneratedKeys);
    }
    @Override
    public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
        return this.origin.execute(sql, columnIndexes);
    }
    @Override
    public boolean execute(final String sql, final String[] columnNames) throws SQLException {
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
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed poolable to %s.",
                    this.source,
                    this.id,
                    poolable
                )
            ).asString()
        );
    }
    @Override
    public boolean isPoolable() throws SQLException {
        return this.origin.isPoolable();
    }
    @Override
    public void closeOnCompletion() throws SQLException {
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] will be closed on completion.",
                    this.source,
                    this.id
                )
            ).asString()
        );
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
