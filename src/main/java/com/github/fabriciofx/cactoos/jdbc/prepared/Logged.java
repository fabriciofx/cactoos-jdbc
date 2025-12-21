/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.prepared;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cactoos.text.FormattedText;
import org.cactoos.text.UncheckedText;

/**
 * Logged PreparedStatement.
 *
 * @checkstyle ParameterNameCheck (1500 lines)
 * @checkstyle ParameterNumberCheck (1500 lines)
 * @since 0.1
 */
@SuppressWarnings(
    {
        "PMD.TooManyMethods",
        "PMD.AvoidDuplicateLiterals",
        "PMD.UnnecessaryLocalRule",
        "PMD.ReplaceJavaUtilDate"
    }
)
public final class Logged extends PreparedStatementEnvelope {
    /**
     * The PreparedStatement.
     */
    private final PreparedStatement origin;

    /**
     * The name of source value.
     */
    private final String source;

    /**
     * The logger.
     */
    private final Logger logger;

    /**
     * The connection level.
     */
    private final Level level;

    /**
     * The PreparedStatement id.
     */
    private final int id;

    /**
     * Ctor.
     *
     * @param stmt Decorated PreparedStatement
     * @param src The name of source data
     * @param lggr The logger
     * @param lvl The connection level
     * @param id The PreparedStatement id
     */
    public Logged(
        final PreparedStatement stmt,
        final String src,
        final Logger lggr,
        final Level lvl,
        final int id
    ) {
        super(stmt);
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
    public void setNull(final int index, final int type) throws SQLException {
        this.origin.setNull(index, type);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%d' value.",
                    this.source,
                    this.id,
                    index,
                    type
                )
            ).asString()
        );
    }

    @Override
    public void setBoolean(
        final int index,
        final boolean value
    ) throws SQLException {
        this.origin.setBoolean(index, value);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%s' value.",
                    this.source,
                    this.id,
                    index,
                    value
                )
            ).asString()
        );
    }

    @Override
    public void setByte(final int index, final byte value) throws SQLException {
        this.origin.setByte(index, value);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%d' value.",
                    this.source,
                    this.id,
                    index,
                    value
                )
            ).asString()
        );
    }

    @Override
    public void setShort(
        final int index,
        final short value
    ) throws SQLException {
        this.origin.setShort(index, value);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%d' value.",
                    this.source,
                    this.id,
                    index,
                    value
                )
            ).asString()
        );
    }

    @Override
    public void setInt(final int index, final int value) throws SQLException {
        this.origin.setInt(index, value);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%d' value.",
                    this.source,
                    this.id,
                    index,
                    value
                )
            ).asString()
        );
    }

    @Override
    public void setLong(final int index, final long value) throws SQLException {
        this.origin.setLong(index, value);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%d' value.",
                    this.source,
                    this.id,
                    index,
                    value
                )
            ).asString()
        );
    }

    @Override
    public void setFloat(
        final int index,
        final float value
    ) throws SQLException {
        this.origin.setFloat(index, value);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%f' value.",
                    this.source,
                    this.id,
                    index,
                    value
                )
            ).asString()
        );
    }

    @Override
    public void setDouble(
        final int index,
        final double value
    ) throws SQLException {
        this.origin.setDouble(index, value);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%f' value.",
                    this.source,
                    this.id,
                    index,
                    value
                )
            ).asString()
        );
    }

    @Override
    public void setBigDecimal(
        final int index,
        final BigDecimal value
    ) throws SQLException {
        this.origin.setBigDecimal(index, value);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%s' value.",
                    this.source,
                    this.id,
                    index,
                    value.toString()
                )
            ).asString()
        );
    }

    @Override
    public void setString(
        final int index,
        final String value
    ) throws SQLException {
        this.origin.setString(index, value);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%s' value.",
                    this.source,
                    this.id,
                    index,
                    value
                )
            ).asString()
        );
    }

    @Override
    public void setBytes(
        final int index,
        final byte[] values
    ) throws SQLException {
        this.origin.setBytes(index, values);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%d' bytes.",
                    this.source,
                    this.id,
                    index,
                    values.length
                )
            ).asString()
        );
    }

    @Override
    public void setDate(final int index, final Date value) throws SQLException {
        this.origin.setDate(index, value);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%s' value.",
                    this.source,
                    this.id,
                    index,
                    value.toString()
                )
            ).asString()
        );
    }

    @Override
    public void setTime(final int index, final Time value) throws SQLException {
        this.origin.setTime(index, value);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%s' value.",
                    this.source,
                    this.id,
                    index,
                    value.toString()
                )
            ).asString()
        );
    }

    @Override
    public void setTimestamp(
        final int index,
        final Timestamp value
    ) throws SQLException {
        this.origin.setTimestamp(index, value);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%s' value.",
                    this.source,
                    this.id,
                    index,
                    value.toString()
                )
            ).asString()
        );
    }

    @Override
    public void setAsciiStream(
        final int index,
        final InputStream stream,
        final int length
    ) throws SQLException {
        this.origin.setAsciiStream(index, stream, length);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%d' bytes.",
                    this.source,
                    this.id,
                    index,
                    length
                )
            ).asString()
        );
    }

    /**
     * Set a stream to Unicode.
     *
     * @param index Parameter index
     * @param stream InputStream
     * @param length Data length
     * @throws SQLException If fails
     * @deprecated It not should be used
     */
    @Deprecated
    public void setUnicodeStream(
        final int index,
        final InputStream stream,
        final int length
    ) throws SQLException {
        this.origin.setUnicodeStream(index, stream, length);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%d' bytes.",
                    this.source,
                    this.id,
                    index,
                    length
                )
            ).asString()
        );
    }

    @Override
    public void setBinaryStream(
        final int index,
        final InputStream stream,
        final int length
    ) throws SQLException {
        this.origin.setBinaryStream(index, stream, length);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%d' bytes.",
                    this.source,
                    this.id,
                    index,
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
    public void setObject(
        final int index,
        final Object value,
        final int type
    ) throws SQLException {
        this.origin.setObject(index, value, type);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%s' data and '%d' type.",
                    this.source,
                    this.id,
                    index,
                    value.toString(),
                    type
                )
            ).asString()
        );
    }

    @Override
    public void setObject(
        final int index,
        final Object value
    ) throws SQLException {
        this.origin.setObject(index, value);
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] changed at parameter[#%d] with '%s' value.",
                    this.source,
                    this.id,
                    index,
                    value.toString()
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
}
