// @checkstyle FileLengthCheck disabled
/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
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
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cactoos.iterable.IterableOf;
import org.cactoos.iterable.Mapped;
import org.cactoos.text.FormattedText;
import org.cactoos.text.Joined;
import org.cactoos.text.UncheckedText;

/**
 * Logged PreparedStatement.
 *
 * @since 0.1
 * @checkstyle ParameterNameCheck (2500 lines)
 * @checkstyle ParameterNumberCheck (2500 lines)
 * @checkstyle ClassFanOutComplexityCheck (2500 lines)
 */
@SuppressWarnings(
    {
        "PMD.TooManyMethods",
        "PMD.UnnecessaryLocalRule",
        "PMD.ReplaceJavaUtilDate",
        "PMD.ExcessivePublicCount",
        "PMD.CouplingBetweenObjects",
        "PMD.ReplaceJavaUtilCalendar"
    }
)
public final class Logged implements PreparedStatement {
    /**
     * The PreparedStatement.
     */
    private final PreparedStatement origin;

    /**
     * Where the logs come from.
     */
    private final String from;

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
     * ResulSet counter.
     */
    private final AtomicInteger resultsets;

    /**
     * Ctor.
     *
     * @param stmt Decorated PreparedStatement
     * @param from Where the logs come from
     * @param lggr The logger
     * @param lvl The connection level
     * @param id The PreparedStatement id
     */
    public Logged(
        final PreparedStatement stmt,
        final String from,
        final Logger lggr,
        final Level lvl,
        final int id
    ) {
        this.origin = stmt;
        this.from = from;
        this.logger = lggr;
        this.level = lvl;
        this.id = id;
        this.resultsets = new AtomicInteger(-1);
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        final Instant begin = Instant.now();
        final ResultSet rset = this.origin.executeQuery();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] retrieved a ResultSet[#%d] \
                    in %dns\
                    """,
                    this.from,
                    this.id,
                    this.resultsets.incrementAndGet(),
                    nanos
                )
            ).asString()
        );
        return new com.github.fabriciofx.cactoos.jdbc.rset.Logged(
            rset,
            this.from,
            this.logger,
            this.level,
            this.resultsets.get()
        );
    }

    @Override
    public int executeUpdate() throws SQLException {
        final Instant begin = Instant.now();
        final int updated = this.origin.executeUpdate();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] executed an update and \
                    returned '%d' in %dns\
                    """,
                    this.from,
                    this.id,
                    updated,
                    nanos
                )
            ).asString()
        );
        return updated;
    }

    @Override
    public void setNull(final int index, final int type) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setNull(index, type);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    null with value '%d' in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    type,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setBoolean(
        final int index,
        final boolean value
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setBoolean(index, value);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    boolean with value '%s' in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    value,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setByte(final int index, final byte value) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setByte(index, value);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    byte with value '%d' in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    value,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setShort(
        final int index,
        final short value
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setShort(index, value);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    short with value '%d' in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    value,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setInt(final int index, final int value) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setInt(index, value);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    int with value '%d' in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    value,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setLong(final int index, final long value) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setLong(index, value);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    long with value '%d' in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    value,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setFloat(
        final int index,
        final float value
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setFloat(index, value);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    float with value '%f' in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    value,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setDouble(
        final int index,
        final double value
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setDouble(index, value);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    double with value '%f' in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    value,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setBigDecimal(
        final int index,
        final BigDecimal value
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setBigDecimal(index, value);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    big decimal with value '%s' in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    value.toString(),
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setString(
        final int index,
        final String value
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setString(index, value);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    string with value '%s' in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    value,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setBytes(
        final int index,
        final byte[] values
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setBytes(index, values);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    bytes with '%d' bytes in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    values.length,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setDate(final int index, final Date value) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setDate(index, value);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    date with value '%s' in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    value.toString(),
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setTime(final int index, final Time value) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setTime(index, value);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    time with value '%s' in %dns
                    """,
                    this.from,
                    this.id,
                    index,
                    value.toString(),
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setTimestamp(
        final int index,
        final Timestamp value
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setTimestamp(index, value);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    timestamp with value '%s' in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    value.toString(),
                    nanos
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
        final Instant begin = Instant.now();
        this.origin.setAsciiStream(index, stream, length);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    ascii stream with '%d' bytes in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    length,
                    nanos
                )
            ).asString()
        );
    }

    @Deprecated
    @Override
    public void setUnicodeStream(
        final int index,
        final InputStream stream,
        final int length
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setUnicodeStream(index, stream, length);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    unicode stream with '%d' bytes\
                    """,
                    this.from,
                    this.id,
                    index,
                    length,
                    nanos
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
        final Instant begin = Instant.now();
        this.origin.setBinaryStream(index, stream, length);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    binary stream with '%d' bytes in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    length,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void clearParameters() throws SQLException {
        final Instant begin = Instant.now();
        this.origin.clearParameters();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] cleaned parameters in %dns",
                    this.from,
                    this.id,
                    nanos
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
        final Instant begin = Instant.now();
        this.origin.setObject(index, value, type);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    object with value '%s' and '%d' type in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    value.toString(),
                    type,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setObject(
        final int index,
        final Object value
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setObject(index, value);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    object with value '%s' in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    value.toString(),
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public boolean execute() throws SQLException {
        final Instant begin = Instant.now();
        final boolean result = this.origin.execute();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] executed and returned \
                    '%s' in %dns\
                    """,
                    this.from,
                    this.id,
                    result,
                    nanos
                )
            ).asString()
        );
        return result;
    }

    @Override
    public void addBatch() throws SQLException {
        final Instant begin = Instant.now();
        this.origin.addBatch();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] added a batch in %dns",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setCharacterStream(
        final int index,
        final Reader reader,
        final int length
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setCharacterStream(index, reader, length);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    character stream with '%d' bytes in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    length,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setRef(final int index, final Ref ref) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setRef(index, ref);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    ref '%s' in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    ref.toString(),
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setBlob(final int index, final Blob blob) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setBlob(index, blob);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    blob with '%d' bytes in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    blob.length(),
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setClob(final int index, final Clob clob) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setClob(index, clob);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    clob with '%d' bytes in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    clob.length(),
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setArray(
        final int index,
        final Array array
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setArray(index, array);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    array of type '%s' in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    array.getBaseTypeName(),
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        final Instant begin = Instant.now();
        final ResultSetMetaData meta = this.origin.getMetaData();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] retrieved \
                    ResultSetMetaData in %dns\
                    """,
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
        return meta;
    }

    @Override
    public void setDate(
        final int index,
        final Date date,
        final Calendar cal
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setDate(index, date, cal);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    date with value '%s' and using calendar in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    date.toString(),
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setTime(
        final int index,
        final Time time,
        final Calendar cal
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setTime(index, time, cal);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    time with value '%s' and using calendar in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    time.toString(),
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setTimestamp(
        final int index,
        final Timestamp timestamp,
        final Calendar cal
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setTimestamp(index, timestamp, cal);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    time with value '%s' and using calendar in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    timestamp.toString(),
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setNull(
        final int index,
        final int type,
        final String name
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setNull(index, type, name);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    null with sql type '%d' and type '%s' in %dns
                    """,
                    this.from,
                    this.id,
                    index,
                    type,
                    name,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setURL(final int index, final URL url) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setURL(index, url);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    url with value '%s' %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    url.toString(),
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        final Instant begin = Instant.now();
        final ParameterMetaData meta = this.origin.getParameterMetaData();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] retrieved \
                    ParameterMetaData in %dns\
                    """,
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
        return meta;
    }

    @Override
    public void setRowId(
        final int index,
        final RowId rowId
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setRowId(index, rowId);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    row id with value '%s' %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    rowId.toString(),
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setNString(
        final int index,
        final String value
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setNString(index, value);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    nstring with value '%s' %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    value,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setNCharacterStream(
        final int index,
        final Reader reader,
        final long length
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setNCharacterStream(index, reader, length);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    ncharacter stream with '%d' bytes in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    length,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setNClob(
        final int index,
        final NClob value
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setNClob(index, value);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    nclob with '%d' bytes in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    value.length(),
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setClob(
        final int index,
        final Reader reader,
        final long length
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setClob(index, reader, length);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    clob with reader and '%d' bytes in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    length,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setBlob(
        final int index,
        final InputStream stream,
        final long length
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setBlob(index, stream, length);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    blob with stream and '%d' bytes in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    length,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setNClob(
        final int index,
        final Reader reader,
        final long length
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setNClob(index, reader, length);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    nclob with reader and '%d' bytes in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    length,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setSQLXML(
        final int index,
        final SQLXML value
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setSQLXML(index, value);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    SQLXML in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setObject(
        final int index,
        final Object value,
        final int type,
        final int scale
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setObject(index, value, type, scale);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    object with sql type '%d' and scale '%d' in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    type,
                    scale,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setAsciiStream(
        final int index,
        final InputStream stream,
        final long length
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setAsciiStream(index, stream, length);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    ascii stream with stream and length '%d' in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    length,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setBinaryStream(
        final int index,
        final InputStream stream,
        final long length
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setBinaryStream(index, stream, length);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    binary stream with stream and length '%d' in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    length,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setCharacterStream(
        final int index,
        final Reader reader,
        final long length
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setCharacterStream(index, reader, length);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    character stream with reader and length '%d' in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    length,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setAsciiStream(
        final int index,
        final InputStream stream
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setAsciiStream(index, stream);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    ascii stream with stream in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setBinaryStream(
        final int index,
        final InputStream stream
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setBinaryStream(index, stream);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    binary stream with stream in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setCharacterStream(
        final int index,
        final Reader reader
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setCharacterStream(index, reader);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \\
                    character stream with reader in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setNCharacterStream(
        final int index,
        final Reader value
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setNCharacterStream(index, value);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    character stream with reader in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setClob(
        final int index,
        final Reader reader
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setClob(index, reader);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    clob with reader in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setBlob(
        final int index,
        final InputStream stream
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setBlob(index, stream);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    blob with stream in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setNClob(
        final int index,
        final Reader reader
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setNClob(index, reader);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed parameter[#%d] to \
                    nclob with reader in %dns\
                    """,
                    this.from,
                    this.id,
                    index,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public ResultSet executeQuery(final String sql) throws SQLException {
        final Instant begin = Instant.now();
        final ResultSet rset = this.origin.executeQuery(sql);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] executed SQL '%s' and \
                    retrieved a ResultSet[#%d] in %dns\
                    """,
                    this.from,
                    this.id,
                    sql,
                    this.resultsets.incrementAndGet(),
                    nanos
                )
            ).asString()
        );
        return new com.github.fabriciofx.cactoos.jdbc.rset.Logged(
            rset,
            this.from,
            this.logger,
            this.level,
            this.resultsets.get()
        );
    }

    @Override
    public int executeUpdate(final String sql) throws SQLException {
        final Instant begin = Instant.now();
        final int updated = this.origin.executeUpdate(sql);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] executed SQL '%s' and \
                    retrieved '%d' in %dns\
                    """,
                    this.from,
                    this.id,
                    sql,
                    updated,
                    nanos
                )
            ).asString()
        );
        return updated;
    }

    @Override
    public void close() throws SQLException {
        final Instant begin = Instant.now();
        this.origin.close();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] closed in %dns",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        final Instant begin = Instant.now();
        final int size = this.origin.getMaxFieldSize();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] retrieved max field \
                    size (%d) in %dns\
                    """,
                    this.from,
                    this.id,
                    size,
                    nanos
                )
            ).asString()
        );
        return size;
    }

    @Override
    public void setMaxFieldSize(final int max) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setMaxFieldSize(max);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed max field size \
                    to '%d' bytes in %dns\
                    """,
                    this.from,
                    this.id,
                    max,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public int getMaxRows() throws SQLException {
        final Instant begin = Instant.now();
        final int max = this.origin.getMaxRows();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] retrieved max rows (%d) in %dns
                    """,
                    this.from,
                    this.id,
                    max,
                    nanos
                )
            ).asString()
        );
        return max;
    }

    @Override
    public void setMaxRows(final int max) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setMaxRows(max);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed max rows to '%d' in %dns
                    """,
                    this.from,
                    this.id,
                    max,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setEscapeProcessing(final boolean enable) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setEscapeProcessing(enable);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed escape processing to \
                    '%s' in %dns\
                    """,
                    this.from,
                    this.id,
                    enable,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        final Instant begin = Instant.now();
        final int timeout = this.origin.getQueryTimeout();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] retrieved query timeout \
                    (%d) in %dns\
                    """,
                    this.from,
                    this.id,
                    timeout,
                    nanos
                )
            ).asString()
        );
        return timeout;
    }

    @Override
    public void setQueryTimeout(final int seconds) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setQueryTimeout(seconds);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed timeout to \
                    '%d' seconds in %dns\
                    """,
                    this.from,
                    this.id,
                    seconds,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void cancel() throws SQLException {
        final Instant begin = Instant.now();
        this.origin.cancel();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] has been canceled in %dns",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        final Instant begin = Instant.now();
        final SQLWarning warning = this.origin.getWarnings();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] retrieved SQL warnings \
                    '%s' in %dns\
                    """,
                    this.from,
                    this.id,
                    warning.getMessage(),
                    nanos
                )
            ).asString()
        );
        return warning;
    }

    @Override
    public void clearWarnings() throws SQLException {
        final Instant begin = Instant.now();
        this.origin.clearWarnings();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] cleared warnings in %dns",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setCursorName(final String name) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setCursorName(name);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed cursor name to \
                    '%s' in %dns\
                    """,
                    this.from,
                    this.id,
                    name,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public boolean execute(final String sql) throws SQLException {
        final Instant begin = Instant.now();
        final boolean result = this.origin.execute(sql);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] executed SQL '%s' and \
                    returned '%d' in %dns
                    """,
                    this.from,
                    this.id,
                    sql,
                    result,
                    nanos
                )
            ).asString()
        );
        return result;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        final Instant begin = Instant.now();
        final ResultSet rset = this.origin.getResultSet();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] get a ResultSet in %dns",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
        return rset;
    }

    @Override
    public int getUpdateCount() throws SQLException {
        final Instant begin = Instant.now();
        final int count = this.origin.getUpdateCount();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] retrieved an update count \
                    (%d) in %dns\
                    """,
                    this.from,
                    this.id,
                    count,
                    nanos
                )
            ).asString()
        );
        return count;
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        final Instant begin = Instant.now();
        final boolean results = this.origin.getMoreResults();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] checked if has more \
                    results (%s) in %dns\
                    """,
                    this.from,
                    this.id,
                    results,
                    nanos
                )
            ).asString()
        );
        return results;
    }

    @Override
    public void setFetchDirection(final int direction) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setFetchSize(direction);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed fetch direction \
                    to '%d' in %dns\
                    """,
                    this.from,
                    this.id,
                    direction,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public int getFetchDirection() throws SQLException {
        final Instant begin = Instant.now();
        final int direction = this.origin.getFetchDirection();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] retrieved fetch direction \
                    (%d) in %dns\
                    """,
                    this.from,
                    this.id,
                    direction,
                    nanos
                )
            ).asString()
        );
        return direction;
    }

    @Override
    public void setFetchSize(final int rows) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setFetchSize(rows);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed fetch size to \
                    '%d' rows in %dns\
                    """,
                    this.from,
                    this.id,
                    rows,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public int getFetchSize() throws SQLException {
        final Instant begin = Instant.now();
        final int size = this.origin.getFetchSize();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] retrieved fetch size \
                    (%d) in %dns\
                    """,
                    this.from,
                    this.id,
                    size,
                    nanos
                )
            ).asString()
        );
        return size;
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        final Instant begin = Instant.now();
        final int concurrency = this.origin.getResultSetConcurrency();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] retrieved a ResultSet \
                    with set concurrency (%d) in %dns\
                    """,
                    this.from,
                    this.id,
                    concurrency,
                    nanos
                )
            ).asString()
        );
        return concurrency;
    }

    @Override
    public int getResultSetType() throws SQLException {
        final Instant begin = Instant.now();
        final int type = this.origin.getResultSetType();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] retrieved a ResultSet type \
                    (%d) in %dns\
                    """,
                    this.from,
                    this.id,
                    type,
                    nanos
                )
            ).asString()
        );
        return type;
    }

    @Override
    public void addBatch(final String sql) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.addBatch(sql);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] added batch with SQL \
                    '%s' in %dns\
                    """,
                    this.from,
                    this.id,
                    sql,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void clearBatch() throws SQLException {
        final Instant begin = Instant.now();
        this.origin.clearBatch();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] cleared batch in %dns",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public int[] executeBatch() throws SQLException {
        final Instant begin = Instant.now();
        final int[] counts = this.origin.executeBatch();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] executed a batch and \
                    returned '%d' counts in %dns\
                    """,
                    this.from,
                    this.id,
                    counts.length,
                    nanos
                )
            ).asString()
        );
        return counts;
    }

    @Override
    public Connection getConnection() throws SQLException {
        final Instant begin = Instant.now();
        final Connection connection = this.origin.getConnection();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] retrieved a connection in %dns
                    """,
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
        return connection;
    }

    @Override
    public boolean getMoreResults(final int current) throws SQLException {
        final Instant begin = Instant.now();
        final boolean results = this.origin.getMoreResults();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] checked if has more results \
                    (%s) in '%d' in %dns\
                    """,
                    this.from,
                    this.id,
                    results,
                    current,
                    nanos
                )
            ).asString()
        );
        return results;
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        final Instant begin = Instant.now();
        final ResultSet rset = this.origin.getGeneratedKeys();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] retrieved a ResultSet[#%d] \
                    with generated keys in %dns\
                    """,
                    this.from,
                    this.id,
                    this.resultsets.incrementAndGet(),
                    nanos
                )
            ).asString()
        );
        return new com.github.fabriciofx.cactoos.jdbc.rset.Logged(
            rset,
            this.from,
            this.logger,
            this.level,
            this.resultsets.get()
        );
    }

    @Override
    public int executeUpdate(
        final String sql,
        final int keys
    ) throws SQLException {
        final Instant begin = Instant.now();
        final int result = this.origin.executeUpdate(sql, keys);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] executed an update using \
                    SQL '%s' with auto generated keys '%d' and \
                    retrieved '%d' in %dns\
                    """,
                    this.from,
                    this.id,
                    sql,
                    keys,
                    result,
                    nanos
                )
            ).asString()
        );
        return result;
    }

    @Override
    public int executeUpdate(
        final String sql,
        final int[] columns
    ) throws SQLException {
        final Instant begin = Instant.now();
        final int result = this.origin.executeUpdate(sql, columns);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] executed an update using \
                    SQL '%s' and columns indexes '%s' in %dns\
                    """,
                    this.from,
                    this.id,
                    sql,
                    new Joined(
                        ", ",
                        new Mapped<>(
                            Object::toString,
                            new IterableOf<>(columns)
                        )
                    ),
                    nanos
                )
            ).asString()
        );
        return result;
    }

    @Override
    public int executeUpdate(
        final String sql,
        final String[] columns
    ) throws SQLException {
        final Instant begin = Instant.now();
        final int result = this.origin.executeUpdate(sql, columns);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] executed an update using \
                    SQL '%s' and columns '%s' in %dns\
                    """,
                    this.from,
                    this.id,
                    sql,
                    new Joined(", ", columns),
                    nanos
                )
            ).asString()
        );
        return result;
    }

    @Override
    public boolean execute(
        final String sql,
        final int keys
    ) throws SQLException {
        final Instant begin = Instant.now();
        final boolean result = this.origin.execute(sql, keys);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] executed using SQL '%s' \
                    and auto generated keys '%d' in %dns\
                    """,
                    this.from,
                    this.id,
                    sql,
                    keys,
                    nanos
                )
            ).asString()
        );
        return result;
    }

    @Override
    public boolean execute(
        final String sql,
        final int[] columns
    ) throws SQLException {
        final Instant begin = Instant.now();
        final boolean result = this.origin.execute(sql, columns);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] executed using SQL '%s' \
                    and columns indexes '%s' in %dns\
                    """,
                    this.from,
                    this.id,
                    sql,
                    new Joined(
                        ", ",
                        new Mapped<>(
                            Object::toString,
                            new IterableOf<>(columns)
                        )
                    ),
                    nanos
                )
            ).asString()
        );
        return result;
    }

    @Override
    public boolean execute(
        final String sql,
        final String[] columns
    ) throws SQLException {
        final Instant begin = Instant.now();
        final boolean result = this.origin.execute(sql, columns);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] executed using SQL '%s' \
                    and columns '%s' in %dns\
                    """,
                    this.from,
                    this.id,
                    sql,
                    new Joined(", ", columns),
                    nanos
                )
            ).asString()
        );
        return result;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        final Instant begin = Instant.now();
        final int holdability = this.origin.getResultSetHoldability();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] retrieved a ResultSet \
                    with holdability (%d) in %dns\
                    """,
                    this.from,
                    this.id,
                    holdability,
                    nanos
                )
            ).asString()
        );
        return holdability;
    }

    @Override
    public boolean isClosed() throws SQLException {
        final Instant begin = Instant.now();
        final boolean closed = this.origin.isClosed();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] checked if is closed \
                    (%d) in %dns\
                    """,
                    this.from,
                    this.id,
                    closed,
                    nanos
                )
            ).asString()
        );
        return closed;
    }

    @Override
    public void setPoolable(final boolean poolable) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setPoolable(poolable);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] changed poolable to '%s' in %dns
                    """,
                    this.from,
                    this.id,
                    poolable,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public boolean isPoolable() throws SQLException {
        final Instant begin = Instant.now();
        final boolean poolable = this.origin.isPoolable();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] checked if is poolable \
                    (%d) in %dns\
                    """,
                    this.from,
                    this.id,
                    poolable,
                    nanos
                )
            ).asString()
        );
        return poolable;
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        final Instant begin = Instant.now();
        this.origin.closeOnCompletion();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] will be closed on \
                    completion in %dns\
                    """,
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        final Instant begin = Instant.now();
        final boolean close = this.origin.isCloseOnCompletion();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] checked if close on \
                    completion (%d) in %dns\
                    """,
                    this.from,
                    this.id,
                    close,
                    nanos
                )
            ).asString()
        );
        return close;
    }

    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        final Instant begin = Instant.now();
        final T wraps = this.origin.unwrap(iface);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] PreparedStatement[#%d] unwrap with '%s' in %dns",
                    this.from,
                    this.id,
                    iface.toString(),
                    nanos
                )
            ).asString()
        );
        return wraps;
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        final Instant begin = Instant.now();
        final boolean wrapped = this.origin.isWrapperFor(iface);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    """
                    [%s] PreparedStatement[#%d] checked if is wrapper for \
                    (%s) with '%s' in %dns\
                    """,
                    this.from,
                    this.id,
                    wrapped,
                    iface.toString(),
                    nanos
                )
            ).asString()
        );
        return wrapped;
    }
}
