// @checkstyle FileLengthCheck disabled
/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.rset;

import com.github.fabriciofx.cactoos.jdbc.Columns;
import com.github.fabriciofx.cactoos.jdbc.Rows;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.cactoos.scalar.Ternary;

/**
 * CachedResultSet.
 *
 * <p>A disconnected, in memory {@link ResultSet}.
 *
 * @since 0.9.0
 * @checkstyle MethodCountCheck (2000 lines)
 * @checkstyle IllegalCatchCheck (2000 lines)
 */
@SuppressWarnings(
    {
        "PMD.TooManyMethods",
        "PMD.ExcessivePublicCount",
        "PMD.AvoidCatchingGenericException",
        "PMD.CouplingBetweenObjects",
        "PMD.AvoidDuplicateLiterals",
        "PMD.ReplaceJavaUtilCalendar",
        "PMD.ReplaceJavaUtilDate"
    }
)
public final class CachedResultSet implements ResultSet {
    /**
     * Rows.
     */
    private final Rows rows;

    /**
     * Columns.
     */
    private final Columns columns;

    /**
     * Cursor.
     */
    private final AtomicInteger cursor;

    /**
     * Controls if ResultSet was closed or not.
     */
    private final AtomicBoolean closed;

    /**
     * Ctor.
     *
     * @param rows The {@link Rows}
     * @param columns The {@link Columns}
     */
    public CachedResultSet(final Rows rows, final Columns columns) {
        this.rows = rows;
        this.columns = columns;
        this.cursor = new AtomicInteger(-1);
        this.closed = new AtomicBoolean(false);
    }

    @Override
    public boolean next() throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        return this.cursor.incrementAndGet() < this.rows.count();
    }

    @Override
    public void close() throws SQLException {
        this.closed.set(true);
    }

    @Override
    public boolean wasNull() throws SQLException {
        return false;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.closed.get();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return new CachedResultSetMetaData(this.columns);
    }

    @Override
    public int findColumn(final String column) throws SQLException {
        try {
            return this.columns.index(column);
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        return this.cursor.get() == -1;
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        return this.cursor.get() > this.rows.count() - 1;
    }

    @Override
    public boolean isFirst() throws SQLException {
        return this.cursor.get() == 0;
    }

    @Override
    public boolean isLast() throws SQLException {
        return this.cursor.get() == this.rows.count() - 1;
    }

    @Override
    public int getRow() throws SQLException {
        return this.cursor.get();
    }

    @Override
    public int getType() throws SQLException {
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    @Override
    public int getConcurrency() throws SQLException {
        return ResultSet.CONCUR_READ_ONLY;
    }

    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return iface.cast(this);
        }
        throw new SQLException("#unwrap(Class<T>): unable to wrap");
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }

    @Override
    public Object getObject(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(column, Object.class);
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public String getString(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                String.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public boolean getBoolean(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            final Boolean value = this.rows.row(this.cursor.get()).value(
                column,
                Boolean.class
            );
            return value != null && value;
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public byte getByte(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            final Byte value = this.rows.row(this.cursor.get()).value(
                column,
                Byte.class
            );
            return new Ternary<>(
                value != null,
                () -> value,
                () -> (byte) 0
            ).value();
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public short getShort(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            final Short value = this.rows.row(this.cursor.get()).value(
                column,
                Short.class
            );
            return new Ternary<>(
                value != null,
                () -> value,
                () -> (short) 0
            ).value();
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public int getInt(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            final Integer value = this.rows.row(this.cursor.get()).value(
                column,
                Integer.class
            );
            return new Ternary<>(
                value != null,
                () -> value,
                () -> 0
            ).value();
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public long getLong(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            final Long value = this.rows.row(this.cursor.get()).value(
                column,
                Long.class
            );
            return new Ternary<>(
                value != null,
                () -> value,
                () -> 0L
            ).value();
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public float getFloat(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            final Float value = this.rows.row(this.cursor.get()).value(
                column,
                Float.class
            );
            return new Ternary<>(
                value != null,
                () -> value,
                () -> 0f
            ).value();
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public double getDouble(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            final Double value = this.rows.row(this.cursor.get()).value(
                column,
                Double.class
            );
            return new Ternary<>(
                value != null,
                () -> value,
                () -> 0.0
            ).value();
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public BigDecimal getBigDecimal(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                BigDecimal.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Deprecated
    @Override
    public BigDecimal getBigDecimal(final String column, final int scale)
        throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                BigDecimal.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public byte[] getBytes(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                byte[].class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Date getDate(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                Date.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Time getTime(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                Time.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Timestamp getTimestamp(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                Timestamp.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Ref getRef(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                Ref.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Blob getBlob(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                Blob.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Clob getClob(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                Clob.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Array getArray(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                Array.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Date getDate(final String column, final Calendar cal)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#getDate(String, Calendar): read-only in memory ResultSet"
        );
    }

    @Override
    public Time getTime(final int index, final Calendar cal)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#getTime(int, Calendar): read-only in memory ResultSet"
        );
    }

    @Override
    public Time getTime(final String column, final Calendar cal)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#getTime(String, Calendar): read-only in memory ResultSet"
        );
    }

    @Override
    public Timestamp getTimestamp(final int index, final Calendar cal)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#getTimestamp(int, Calendar): read-only in memory ResultSet"
        );
    }

    @Override
    public Timestamp getTimestamp(final String column, final Calendar cal)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#getTimestamp(String, Calendar): read-only in memory ResultSet"
        );
    }

    @Override
    public URL getURL(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                URL.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Object getObject(
        final String column,
        final Map<String, Class<?>> map
    ) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                map.values().iterator().next()
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public <T> T getObject(final String column, final Class<T> type)
        throws SQLException {
        return type.cast(this.getObject(column));
    }

    @Override
    public String getNString(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                String.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Reader getNCharacterStream(final String column)
        throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                Reader.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public InputStream getAsciiStream(final String column)
        throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                InputStream.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Deprecated
    @Override
    public InputStream getUnicodeStream(final String column)
        throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                InputStream.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public InputStream getBinaryStream(final String column)
        throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                InputStream.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Reader getCharacterStream(final String column)
        throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                Reader.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public NClob getNClob(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                NClob.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public SQLXML getSQLXML(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                SQLXML.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public RowId getRowId(final String column) throws SQLException {
        if (this.closed.get()) {
            throw new SQLException("ResultSet is already closed");
        }
        try {
            return this.rows.row(this.cursor.get()).value(
                column,
                RowId.class
            );
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Object getObject(final int index) throws SQLException {
        try {
            return this.getObject(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public String getString(final int index) throws SQLException {
        try {
            return this.getString(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public boolean getBoolean(final int index) throws SQLException {
        try {
            return this.getBoolean(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public byte getByte(final int index) throws SQLException {
        try {
            return this.getByte(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public short getShort(final int index) throws SQLException {
        try {
            return this.getShort(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public int getInt(final int index) throws SQLException {
        try {
            return this.getInt(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public long getLong(final int index) throws SQLException {
        try {
            return this.getLong(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public float getFloat(final int index) throws SQLException {
        try {
            return this.getFloat(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public double getDouble(final int index) throws SQLException {
        try {
            return this.getDouble(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public BigDecimal getBigDecimal(final int index) throws SQLException {
        try {
            return this.getBigDecimal(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Deprecated
    @Override
    public BigDecimal getBigDecimal(final int index, final int scale)
        throws SQLException {
        try {
            return this.getBigDecimal(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public byte[] getBytes(final int index) throws SQLException {
        try {
            return this.getBytes(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Date getDate(final int index) throws SQLException {
        try {
            return this.getDate(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Time getTime(final int index) throws SQLException {
        try {
            return this.getTime(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Timestamp getTimestamp(final int index) throws SQLException {
        try {
            return this.getTimestamp(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Date getDate(final int index, final Calendar cal)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#getDate(int, Calendar): read-only in memory ResultSet"
        );
    }

    @Override
    public URL getURL(final int index) throws SQLException {
        try {
            return this.getURL(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Ref getRef(final int index) throws SQLException {
        try {
            return this.getRef(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Blob getBlob(final int index) throws SQLException {
        try {
            return this.getBlob(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Clob getClob(final int index) throws SQLException {
        try {
            return this.getClob(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Array getArray(final int index) throws SQLException {
        try {
            return this.getArray(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Object getObject(
        final int index,
        final Map<String, Class<?>> map
    ) throws SQLException {
        try {
            return this.getObject(this.columns.name(index), map);
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public <T> T getObject(final int index, final Class<T> type)
        throws SQLException {
        return type.cast(this.getObject(index));
    }

    @Override
    public InputStream getAsciiStream(final int index)
        throws SQLException {
        try {
            return this.getAsciiStream(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Deprecated
    @Override
    public InputStream getUnicodeStream(final int index)
        throws SQLException {
        try {
            return this.getUnicodeStream(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public InputStream getBinaryStream(final int index)
        throws SQLException {
        try {
            return this.getBinaryStream(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Reader getCharacterStream(final int index)
        throws SQLException {
        try {
            return this.getCharacterStream(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public String getNString(final int index) throws SQLException {
        try {
            return this.getNString(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Reader getNCharacterStream(final int index)
        throws SQLException {
        try {
            return this.getNCharacterStream(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public NClob getNClob(final int index) throws SQLException {
        try {
            return this.getNClob(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public SQLXML getSQLXML(final int index) throws SQLException {
        try {
            return this.getSQLXML(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public RowId getRowId(final int index) throws SQLException {
        try {
            return this.getRowId(this.columns.name(index));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        throw new UnsupportedOperationException(
            "#getWarnings(): read-only in memory ResultSet"
        );
    }

    @Override
    public void clearWarnings() throws SQLException {
        throw new UnsupportedOperationException(
            "#clearWarnings(): read-only in memory ResultSet"
        );
    }

    @Override
    public String getCursorName() throws SQLException {
        throw new UnsupportedOperationException(
            "#getCursorName(): read-only in memory ResultSet"
        );
    }

    @Override
    public void beforeFirst() throws SQLException {
        throw new SQLException(
            "#beforeFirst(): operation not allowed for a forward-only ResultSet"
        );
    }

    @Override
    public void afterLast() throws SQLException {
        throw new SQLException(
            "#afterLast(): operation not allowed for a forward-only ResultSet"
        );
    }

    @Override
    public boolean first() throws SQLException {
        throw new SQLException(
            "#first(): operation not allowed for a forward-only ResultSet"
        );
    }

    @Override
    public boolean last() throws SQLException {
        throw new SQLException(
            "#last(): operation not allowed for a forward-only ResultSet"
        );
    }

    @Override
    public boolean absolute(final int row) throws SQLException {
        throw new SQLException(
            "#absolute(int): operation not allowed for a forward-only ResultSet"
        );
    }

    @Override
    public boolean relative(final int num) throws SQLException {
        throw new UnsupportedOperationException(
            "#relative(int): read-only in memory ResultSet"
        );
    }

    @Override
    public boolean previous() throws SQLException {
        throw new SQLException(
            "#previous(): operation not allowed for a forward-only ResultSet"
        );
    }

    @Override
    public void setFetchDirection(final int direction) throws SQLException {
        throw new UnsupportedOperationException(
            "#setFetchDirection(int): read-only in memory ResultSet"
        );
    }

    @Override
    public int getFetchDirection() throws SQLException {
        throw new UnsupportedOperationException(
            "#getFetchDirection(): read-only in memory ResultSet"
        );
    }

    @Override
    public void setFetchSize(final int size) throws SQLException {
        throw new UnsupportedOperationException(
            "#setFetchSize(int): read-only in memory ResultSet"
        );
    }

    @Override
    public int getFetchSize() throws SQLException {
        throw new UnsupportedOperationException(
            "#getFetchSize(): read-only in memory ResultSet"
        );
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        throw new UnsupportedOperationException(
            "#rowUpdated(): read-only in memory ResultSet"
        );
    }

    @Override
    public boolean rowInserted() throws SQLException {
        throw new UnsupportedOperationException(
            "#rowInserted(): read-only in memory ResultSet"
        );
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        throw new UnsupportedOperationException(
            "#rowDeleted(): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateNull(final int index) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateNull(int): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateBoolean(final int index, final boolean value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateBoolean(int, boolean): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateByte(final int index, final byte value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateByte(int, byte): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateShort(final int index, final short value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateShort(int, short): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateInt(final int index, final int value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateInt(int, int): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateLong(final int index, final long value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateLong(int, long): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateFloat(final int index, final float value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateFloat(int, float): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateDouble(final int index, final double value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateDouble(int, double): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateBigDecimal(final int index, final BigDecimal value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateBigDecimal(int, BigDecimal): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateString(final int index, final String value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateString(int, String): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateBytes(final int index, final byte[] value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateBytes(int, byte[]): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateDate(final int index, final Date value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateDate(int, Date): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateTime(final int index, final Time value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateTime(int, Time): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateTimestamp(final int index, final Timestamp value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateTimestamp(int, Timestamp): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateAsciiStream(
        final int index,
        final InputStream value,
        final int length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateAsciiStream(int, InputStream, int): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateBinaryStream(
        final int index,
        final InputStream value,
        final int length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateBinaryStream(int, InputStream, int): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateCharacterStream(
        final int index,
        final Reader value,
        final int length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateCharacterStream(int, Reader, int): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateObject(
        final int index,
        final Object value,
        final int scale
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateObject(int, Object, int): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateObject(final int index, final Object value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateObject(int, Object): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateNull(final String column) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateNull(String): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateBoolean(final String column, final boolean value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateBoolean(String, boolean): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateByte(final String column, final byte value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateByte(String, byte): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateShort(final String column, final short value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateShort(String, short): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateInt(final String column, final int value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateInt(String, int): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateLong(final String column, final long value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateLong(String, long): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateFloat(final String column, final float value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateFloat(String, float): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateDouble(final String column, final double value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateDouble(String, double): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateBigDecimal(final String column, final BigDecimal value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateBigDecimal(String, BigDecimal): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateString(final String column, final String value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateString(String, String): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateBytes(final String column, final byte[] value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateBytes(String, byte[]): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateDate(final String column, final Date value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateDate(String, Date): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateTime(final String column, final Time value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateTime(String, Time): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateTimestamp(final String column, final Timestamp value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateTimestamp(String, Timestamp): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateAsciiStream(
        final String column,
        final InputStream value,
        final int length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateAsciiStream(String, InputStream, int): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateBinaryStream(
        final String column,
        final InputStream value,
        final int length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateBinaryStream(String, InputStream, int): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateCharacterStream(
        final String column,
        final Reader reader,
        final int length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateCharacterStream(String, Reader, length): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateObject(
        final String column,
        final Object value,
        final int scale
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateObject(String, Object, int): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateObject(final String column, final Object value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateObject(String, Object): read-only in memory ResultSet"
        );
    }

    @Override
    public void insertRow() throws SQLException {
        throw new UnsupportedOperationException(
            "#insertRow(): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateRow() throws SQLException {
        throw new UnsupportedOperationException(
            "#updateRow(): read-only in memory ResultSet"
        );
    }

    @Override
    public void deleteRow() throws SQLException {
        throw new UnsupportedOperationException(
            "#deleteRow(): read-only in memory ResultSet"
        );
    }

    @Override
    public void refreshRow() throws SQLException {
        throw new UnsupportedOperationException(
            "#refreshRow(): read-only in memory ResultSet"
        );
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        throw new UnsupportedOperationException(
            "#cancelRowUpdates(): read-only in memory ResultSet"
        );
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        throw new UnsupportedOperationException(
            "#moveToInsertRow(): read-only in memory ResultSet"
        );
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        throw new UnsupportedOperationException(
            "#moveToCurrentRow(): read-only in memory ResultSet"
        );
    }

    @Override
    public Statement getStatement() throws SQLException {
        throw new UnsupportedOperationException(
            "#getStatement(): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateRef(final int index, final Ref value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateRef(int, Ref): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateRef(final String column, final Ref value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateRef(String, Ref): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateBlob(final int index, final Blob value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateBlob(int, Blob): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateBlob(final String column, final Blob value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateBlob(String, Blob): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateClob(final int index, final Clob value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateClob(int, Clob): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateClob(final String column, final Clob value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateClob(String, Clob): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateArray(final int index, final Array value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateArray(int, Array): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateArray(final String column, final Array value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateArray(String, Array): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateRowId(final int index, final RowId value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateRowId(int, RowId): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateRowId(final String column, final RowId value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateRowId(): read-only in memory ResultSet"
        );
    }

    @Override
    public int getHoldability() throws SQLException {
        throw new UnsupportedOperationException(
            "#getHoldability(): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateNString(final int index, final String value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateNString(int, String): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateNString(final String column, final String value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateNString(String, String): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateNClob(final int index, final NClob value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateNClob(int, NClob): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateNClob(final String column, final NClob value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateNClob(String, NClob): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateSQLXML(final int index, final SQLXML value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateSQLXML(int, SQLXML): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateSQLXML(final String column, final SQLXML value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateSQLXML(String, SQLXML): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateNCharacterStream(
        final int index,
        final Reader value,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateNCharacterStream(int, Reader, long): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateNCharacterStream(
        final String column,
        final Reader reader,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateNCharacterStream(String, Reader, long): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateAsciiStream(
        final int index,
        final InputStream value,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateAsciiStream(int, InputStream, long): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateBinaryStream(
        final int index,
        final InputStream value,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateBinaryStream(int, InputStream, long): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateCharacterStream(
        final int index,
        final Reader value,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateCharacterStream(int, Reader, long): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateAsciiStream(
        final String column,
        final InputStream value,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateAsciiStream(String, InputStream, long): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateBinaryStream(
        final String column,
        final InputStream value,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateBinaryStream(String, InputStream, long): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateCharacterStream(
        final String column,
        final Reader reader,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateCharacterStream(String, Reader, long): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateBlob(
        final int index,
        final InputStream value,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateBlob(int, InputStream, long): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateBlob(
        final String column,
        final InputStream value,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateBlob(String, InputStream, long): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateClob(
        final int index,
        final Reader reader,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateClob(int, Reader, long): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateClob(
        final String column,
        final Reader reader,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateClob(String, Reader, long): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateNClob(
        final int index,
        final Reader reader,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateNClob(int, Reader, long): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateNClob(
        final String column,
        final Reader reader,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateNClob(String, Reader, long): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateNCharacterStream(final int index, final Reader value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateNCharacterStream(int, Reader): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateNCharacterStream(
        final String column,
        final Reader reader
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateNCharacterStream(String, Reader): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateAsciiStream(final int index, final InputStream value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateAsciiStream(int, InputStream): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateBinaryStream(final int index, final InputStream value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateBinaryStream(int, InputStream): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateCharacterStream(final int index, final Reader value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateCharacterStream(int, Reader): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateAsciiStream(final String column, final InputStream value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateAsciiStream(String, InputStream): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateBinaryStream(
        final String column,
        final InputStream value
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateBinaryStream(String, InputStream): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateCharacterStream(
        final String column,
        final Reader reader
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateCharacterStream(String, Reader): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateBlob(final int index, final InputStream value)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateBlob(int, InputStream): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateBlob(
        final String column,
        final InputStream value
    ) throws SQLException {
        throw new UnsupportedOperationException(
            "#updateBlob(String, InputStream): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateClob(final int index, final Reader reader)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateClob(int, Reader): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateClob(final String column, final Reader reader)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateClob(String, Reader): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateNClob(final int index, final Reader reader)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateNClob(int, Reader): read-only in memory ResultSet"
        );
    }

    @Override
    public void updateNClob(final String column, final Reader reader)
        throws SQLException {
        throw new UnsupportedOperationException(
            "#updateNClob(String, Reader): read-only in memory ResultSet"
        );
    }
}
