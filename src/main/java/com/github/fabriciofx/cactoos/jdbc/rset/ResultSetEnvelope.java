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
package com.github.fabriciofx.cactoos.jdbc.rset;

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

/**
 * ResultSet Envelope.
 *
 * @since 0.4
 * @checkstyle ParameterNameCheck (1500 lines)
 * @checkstyle MethodCountCheck (1500 lines)
 * @checkstyle DesignForExtensionCheck (1500 lines)
 * @checkstyle EmptyLineSeparatorCheck (1500 lines)
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
        "PMD.AvoidUsingShortType",
        "PMD.ExcessiveClassLength"
    }
)
public abstract class ResultSetEnvelope implements ResultSet {
    /**
     * ResultSet to be decorated.
     */
    private final ResultSet origin;

    /**
     * Ctor.
     * @param rset Original ResultSet to be decorated
     */
    public ResultSetEnvelope(final ResultSet rset) {
        this.origin = rset;
    }

    @Override
    public boolean next() throws SQLException {
        return this.origin.next();
    }
    @Override
    public void close() throws SQLException {
        this.origin.close();
    }
    @Override
    public boolean wasNull() throws SQLException {
        return this.origin.wasNull();
    }
    @Override
    public String getString(final int index) throws SQLException {
        return this.origin.getString(index);
    }
    @Override
    public boolean getBoolean(final int index) throws SQLException {
        return this.origin.getBoolean(index);
    }
    @Override
    public byte getByte(final int index) throws SQLException {
        return this.origin.getByte(index);
    }
    @Override
    public short getShort(final int index) throws SQLException {
        return this.origin.getShort(index);
    }
    @Override
    public int getInt(final int index) throws SQLException {
        return this.origin.getInt(index);
    }
    @Override
    public long getLong(final int index) throws SQLException {
        return this.origin.getLong(index);
    }
    @Override
    public float getFloat(final int index) throws SQLException {
        return this.origin.getFloat(index);
    }
    @Override
    public double getDouble(final int index) throws SQLException {
        return this.origin.getDouble(index);
    }
    /**
     * Get a BigDecimal.
     * @deprecated It not should be used
     * @param index Column index
     * @param scale Scale
     * @return A BigDecimal number
     * @throws SQLException If fails
     */
    @Deprecated
    public BigDecimal getBigDecimal(final int index, final int scale) throws SQLException {
        return this.origin.getBigDecimal(index, scale);
    }
    @Override
    public byte[] getBytes(final int index) throws SQLException {
        return this.origin.getBytes(index);
    }
    @Override
    public Date getDate(final int index) throws SQLException {
        return this.origin.getDate(index);
    }
    @Override
    public Time getTime(final int index) throws SQLException {
        return this.origin.getTime(index);
    }
    @Override
    public Timestamp getTimestamp(final int index) throws SQLException {
        return this.origin.getTimestamp(index);
    }
    @Override
    public InputStream getAsciiStream(final int index) throws SQLException {
        return this.origin.getAsciiStream(index);
    }
    /**
     * Get a stream in Unicode.
     * @deprecated It not should be used
     * @param index Column index
     * @return An InputStream number
     * @throws SQLException If fails
     */
    @Deprecated
    public InputStream getUnicodeStream(final int index) throws SQLException {
        return this.origin.getUnicodeStream(index);
    }
    @Override
    public InputStream getBinaryStream(final int index) throws SQLException {
        return this.origin.getBinaryStream(index);
    }
    @Override
    public String getString(final String label) throws SQLException {
        return this.origin.getString(label);
    }
    @Override
    public boolean getBoolean(final String label) throws SQLException {
        return this.origin.getBoolean(label);
    }
    @Override
    public byte getByte(final String label) throws SQLException {
        return this.origin.getByte(label);
    }
    @Override
    public short getShort(final String label) throws SQLException {
        return this.origin.getShort(label);
    }
    @Override
    public int getInt(final String label) throws SQLException {
        return this.origin.getInt(label);
    }
    @Override
    public long getLong(final String label) throws SQLException {
        return this.origin.getLong(label);
    }
    @Override
    public float getFloat(final String label) throws SQLException {
        return this.origin.getFloat(label);
    }
    @Override
    public double getDouble(final String label) throws SQLException {
        return this.origin.getDouble(label);
    }
    /**
     * Get a BigDecimal.
     * @deprecated It not should be used
     * @param label Column label
     * @param scale Scale
     * @return A BigDecimal number
     * @throws SQLException If fails
     */
    @Deprecated
    public BigDecimal getBigDecimal(final String label, final int scale) throws SQLException {
        return this.origin.getBigDecimal(label, scale);
    }
    @Override
    public byte[] getBytes(final String label) throws SQLException {
        return this.origin.getBytes(label);
    }
    @Override
    public Date getDate(final String label) throws SQLException {
        return this.origin.getDate(label);
    }
    @Override
    public Time getTime(final String label) throws SQLException {
        return this.origin.getTime(label);
    }
    @Override
    public Timestamp getTimestamp(final String label) throws SQLException {
        return this.origin.getTimestamp(label);
    }
    @Override
    public InputStream getAsciiStream(final String label) throws SQLException {
        return this.origin.getAsciiStream(label);
    }
    /**
     * Get a stream in Unicode.
     * @deprecated It not should be used
     * @param label Column label
     * @return An InputStream data
     * @throws SQLException If fails
     */
    @Deprecated
    public InputStream getUnicodeStream(final String label) throws SQLException {
        return this.origin.getUnicodeStream(label);
    }
    @Override
    public InputStream getBinaryStream(final String label) throws SQLException {
        return this.origin.getBinaryStream(label);
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
    public String getCursorName() throws SQLException {
        return this.origin.getCursorName();
    }
    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return this.origin.getMetaData();
    }
    @Override
    public Object getObject(final int index) throws SQLException {
        return this.origin.getObject(index);
    }
    @Override
    public Object getObject(final String label) throws SQLException {
        return this.origin.getObject(label);
    }
    @Override
    public int findColumn(final String label) throws SQLException {
        return this.origin.findColumn(label);
    }
    @Override
    public Reader getCharacterStream(final int index) throws SQLException {
        return this.origin.getCharacterStream(index);
    }
    @Override
    public Reader getCharacterStream(final String label) throws SQLException {
        return this.origin.getCharacterStream(label);
    }
    @Override
    public BigDecimal getBigDecimal(final int index) throws SQLException {
        return this.origin.getBigDecimal(index);
    }
    @Override
    public BigDecimal getBigDecimal(final String label) throws SQLException {
        return this.origin.getBigDecimal(label);
    }
    @Override
    public boolean isBeforeFirst() throws SQLException {
        return this.origin.isBeforeFirst();
    }
    @Override
    public boolean isAfterLast() throws SQLException {
        return this.origin.isAfterLast();
    }
    @Override
    public boolean isFirst() throws SQLException {
        return this.origin.isFirst();
    }
    @Override
    public boolean isLast() throws SQLException {
        return this.origin.isLast();
    }
    @Override
    public void beforeFirst() throws SQLException {
        this.origin.beforeFirst();
    }
    @Override
    public void afterLast() throws SQLException {
        this.origin.afterLast();
    }
    @Override
    public boolean first() throws SQLException {
        return this.origin.first();
    }
    @Override
    public boolean last() throws SQLException {
        return this.origin.last();
    }
    @Override
    public int getRow() throws SQLException {
        return this.origin.getRow();
    }
    @Override
    public boolean absolute(final int row) throws SQLException {
        return this.origin.absolute(row);
    }
    @Override
    public boolean relative(final int rows) throws SQLException {
        return this.origin.relative(rows);
    }
    @Override
    public boolean previous() throws SQLException {
        return this.origin.previous();
    }
    @Override
    public void setFetchDirection(final int direction) throws SQLException {
        this.origin.setFetchDirection(direction);
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
    public int getType() throws SQLException {
        return this.origin.getType();
    }
    @Override
    public int getConcurrency() throws SQLException {
        return this.origin.getConcurrency();
    }
    @Override
    public boolean rowUpdated() throws SQLException {
        return this.origin.rowUpdated();
    }
    @Override
    public boolean rowInserted() throws SQLException {
        return this.origin.rowInserted();
    }
    @Override
    public boolean rowDeleted() throws SQLException {
        return this.origin.rowDeleted();
    }
    @Override
    public void updateNull(final int index) throws SQLException {
        this.origin.updateNull(index);
    }
    @Override
    public void updateBoolean(final int index, final boolean value) throws SQLException {
        this.origin.updateBoolean(index, value);
    }
    @Override
    public void updateByte(final int index, final byte value) throws SQLException {
        this.origin.updateByte(index, value);
    }
    @Override
    public void updateShort(final int index, final short value) throws SQLException {
        this.origin.updateShort(index, value);
    }
    @Override
    public void updateInt(final int index, final int value) throws SQLException {
        this.origin.updateInt(index, value);
    }
    @Override
    public void updateLong(final int index, final long value) throws SQLException {
        this.origin.updateLong(index, value);
    }
    @Override
    public void updateFloat(final int index, final float value) throws SQLException {
        this.origin.updateFloat(index, value);
    }
    @Override
    public void updateDouble(final int index, final double value) throws SQLException {
        this.origin.updateDouble(index, value);
    }
    @Override
    public void updateBigDecimal(final int index, final BigDecimal value) throws SQLException {
        this.origin.updateBigDecimal(index, value);
    }
    @Override
    public void updateString(final int index, final String value) throws SQLException {
        this.origin.updateString(index, value);
    }
    @Override
    public void updateBytes(final int index, final byte[] values) throws SQLException {
        this.origin.updateBytes(index, values);
    }
    @Override
    public void updateDate(final int index, final Date value) throws SQLException {
        this.origin.updateDate(index, value);
    }
    @Override
    public void updateTime(final int index, final Time value) throws SQLException {
        this.origin.updateTime(index, value);
    }
    @Override
    public void updateTimestamp(final int index, final Timestamp value) throws SQLException {
        this.origin.updateTimestamp(index, value);
    }
    @Override
    public void updateAsciiStream(final int index, final InputStream value, final int length) throws SQLException {
        this.origin.updateAsciiStream(index, value, length);
    }
    @Override
    public void updateBinaryStream(final int index, final InputStream value, final int length) throws SQLException {
        this.origin.updateBinaryStream(index, value, length);
    }
    @Override
    public void updateCharacterStream(final int index, final Reader value, final int length) throws SQLException {
        this.origin.updateCharacterStream(index, value, length);
    }
    @Override
    public void updateObject(final int index, final Object value, final int scaleOrLength) throws SQLException {
        this.origin.updateObject(index, value, scaleOrLength);
    }
    @Override
    public void updateObject(final int index, final Object value) throws SQLException {
        this.origin.updateObject(index, value);
    }
    @Override
    public void updateNull(final String label) throws SQLException {
        this.origin.updateNull(label);
    }
    @Override
    public void updateBoolean(final String label, final boolean value) throws SQLException {
        this.origin.updateBoolean(label, value);
    }
    @Override
    public void updateByte(final String label, final byte value) throws SQLException {
        this.origin.updateByte(label, value);
    }
    @Override
    public void updateShort(final String label, final short value) throws SQLException {
        this.origin.updateShort(label, value);
    }
    @Override
    public void updateInt(final String label, final int value) throws SQLException {
        this.origin.updateInt(label, value);
    }
    @Override
    public void updateLong(final String label, final long value) throws SQLException {
        this.origin.updateLong(label, value);
    }
    @Override
    public void updateFloat(final String label, final float value) throws SQLException {
        this.origin.updateFloat(label, value);
    }
    @Override
    public void updateDouble(final String label, final double value) throws SQLException {
        this.origin.updateDouble(label, value);
    }
    @Override
    public void updateBigDecimal(final String label, final BigDecimal value) throws SQLException {
        this.origin.updateBigDecimal(label, value);
    }
    @Override
    public void updateString(final String label, final String value) throws SQLException {
        this.origin.updateString(label, value);
    }
    @Override
    public void updateBytes(final String label, final byte[] value) throws SQLException {
        this.origin.updateBytes(label, value);
    }
    @Override
    public void updateDate(final String label, final Date value) throws SQLException {
        this.origin.updateDate(label, value);
    }
    @Override
    public void updateTime(final String label, final Time value) throws SQLException {
        this.origin.updateTime(label, value);
    }
    @Override
    public void updateTimestamp(final String label, final Timestamp value) throws SQLException {
        this.origin.updateTimestamp(label, value);
    }
    @Override
    public void updateAsciiStream(final String label, final InputStream value, final int length) throws SQLException {
        this.origin.updateAsciiStream(label, value, length);
    }
    @Override
    public void updateBinaryStream(final String label, final InputStream value, final int length) throws SQLException {
        this.origin.updateBinaryStream(label, value, length);
    }
    @Override
    public void updateCharacterStream(final String label, final Reader reader, final int length) throws SQLException {
        this.origin.updateCharacterStream(label, reader, length);
    }
    @Override
    public void updateObject(final String label, final Object value, final int scaleOrLength) throws SQLException {
        this.origin.updateObject(label, value, scaleOrLength);
    }
    @Override
    public void updateObject(final String label, final Object value) throws SQLException {
        this.origin.updateObject(label, value);
    }
    @Override
    public void insertRow() throws SQLException {
        this.origin.insertRow();
    }
    @Override
    public void updateRow() throws SQLException {
        this.origin.updateRow();
    }
    @Override
    public void deleteRow() throws SQLException {
        this.origin.deleteRow();
    }
    @Override
    public void refreshRow() throws SQLException {
        this.origin.refreshRow();
    }
    @Override
    public void cancelRowUpdates() throws SQLException {
        this.origin.cancelRowUpdates();
    }
    @Override
    public void moveToInsertRow() throws SQLException {
        this.origin.moveToInsertRow();
    }
    @Override
    public void moveToCurrentRow() throws SQLException {
        this.origin.moveToCurrentRow();
    }
    @Override
    public Statement getStatement() throws SQLException {
        return this.origin.getStatement();
    }
    @Override
    public Object getObject(final int index, final Map<String, Class<?>> map) throws SQLException {
        return this.origin.getObject(index, map);
    }
    @Override
    public Ref getRef(final int index) throws SQLException {
        return this.origin.getRef(index);
    }
    @Override
    public Blob getBlob(final int index) throws SQLException {
        return this.origin.getBlob(index);
    }
    @Override
    public Clob getClob(final int index) throws SQLException {
        return this.origin.getClob(index);
    }
    @Override
    public Array getArray(final int index) throws SQLException {
        return this.origin.getArray(index);
    }
    @Override
    public Object getObject(final String label, final Map<String, Class<?>> map) throws SQLException {
        return this.origin.getObject(label, map);
    }
    @Override
    public Ref getRef(final String label) throws SQLException {
        return this.origin.getRef(label);
    }
    @Override
    public Blob getBlob(final String label) throws SQLException {
        return this.origin.getBlob(label);
    }
    @Override
    public Clob getClob(final String label) throws SQLException {
        return this.origin.getClob(label);
    }
    @Override
    public Array getArray(final String label) throws SQLException {
        return this.origin.getArray(label);
    }
    @Override
    public Date getDate(final int index, final Calendar cal) throws SQLException {
        return this.origin.getDate(index, cal);
    }
    @Override
    public Date getDate(final String label, final Calendar cal) throws SQLException {
        return this.origin.getDate(label, cal);
    }
    @Override
    public Time getTime(final int index, final Calendar cal) throws SQLException {
        return this.origin.getTime(index, cal);
    }
    @Override
    public Time getTime(final String label, final Calendar cal) throws SQLException {
        return this.origin.getTime(label, cal);
    }
    @Override
    public Timestamp getTimestamp(final int index, final Calendar cal) throws SQLException {
        return this.origin.getTimestamp(index, cal);
    }
    @Override
    public Timestamp getTimestamp(final String label, final Calendar cal) throws SQLException {
        return this.origin.getTimestamp(label, cal);
    }
    @Override
    public URL getURL(final int index) throws SQLException {
        return this.origin.getURL(index);
    }
    @Override
    public URL getURL(final String label) throws SQLException {
        return this.origin.getURL(label);
    }
    @Override
    public void updateRef(final int index, final Ref value) throws SQLException {
        this.origin.updateRef(index, value);
    }
    @Override
    public void updateRef(final String label, final Ref value) throws SQLException {
        this.origin.updateRef(label, value);
    }
    @Override
    public void updateBlob(final int index, final Blob value) throws SQLException {
        this.origin.updateBlob(index, value);
    }
    @Override
    public void updateBlob(final String label, final Blob value) throws SQLException {
        this.origin.updateBlob(label, value);
    }
    @Override
    public void updateClob(final int index, final Clob value) throws SQLException {
        this.origin.updateClob(index, value);
    }
    @Override
    public void updateClob(final String label, final Clob value) throws SQLException {
        this.origin.updateClob(label, value);
    }
    @Override
    public void updateArray(final int index, final Array value) throws SQLException {
        this.origin.updateArray(index, value);
    }
    @Override
    public void updateArray(final String label, final Array value) throws SQLException {
        this.origin.updateArray(label, value);
    }
    @Override
    public RowId getRowId(final int index) throws SQLException {
        return this.origin.getRowId(index);
    }
    @Override
    public RowId getRowId(final String label) throws SQLException {
        return this.origin.getRowId(label);
    }
    @Override
    public void updateRowId(final int index, final RowId value) throws SQLException {
        this.origin.updateRowId(index, value);
    }
    @Override
    public void updateRowId(final String label, final RowId value) throws SQLException {
        this.origin.updateRowId(label, value);
    }
    @Override
    public int getHoldability() throws SQLException {
        return this.origin.getHoldability();
    }
    @Override
    public boolean isClosed() throws SQLException {
        return this.origin.isClosed();
    }
    @Override
    public void updateNString(final int index, final String nString) throws SQLException {
        this.origin.updateNString(index, nString);
    }
    @Override
    public void updateNString(final String label, final String nString) throws SQLException {
        this.origin.updateNString(label, nString);
    }
    @Override
    public void updateNClob(final int index, final NClob nClob) throws SQLException {
        this.origin.updateNClob(index, nClob);
    }
    @Override
    public void updateNClob(final String label, final NClob nClob) throws SQLException {
        this.origin.updateNClob(label, nClob);
    }
    @Override
    public NClob getNClob(final int index) throws SQLException {
        return this.origin.getNClob(index);
    }
    @Override
    public NClob getNClob(final String label) throws SQLException {
        return this.origin.getNClob(label);
    }
    @Override
    public SQLXML getSQLXML(final int index) throws SQLException {
        return this.origin.getSQLXML(index);
    }
    @Override
    public SQLXML getSQLXML(final String label) throws SQLException {
        return this.origin.getSQLXML(label);
    }
    @Override
    public void updateSQLXML(final int index, final SQLXML value) throws SQLException {
        this.origin.updateSQLXML(index, value);
    }
    @Override
    public void updateSQLXML(final String label, final SQLXML value) throws SQLException {
        this.origin.updateSQLXML(label, value);
    }
    @Override
    public String getNString(final int index) throws SQLException {
        return this.origin.getNString(index);
    }
    @Override
    public String getNString(final String label) throws SQLException {
        return this.origin.getNString(label);
    }
    @Override
    public Reader getNCharacterStream(final int index) throws SQLException {
        return this.origin.getNCharacterStream(index);
    }
    @Override
    public Reader getNCharacterStream(final String label) throws SQLException {
        return this.origin.getNCharacterStream(label);
    }
    @Override
    public void updateNCharacterStream(final int index, final Reader value, final long length) throws SQLException {
        this.origin.updateNCharacterStream(index, value, length);
    }
    @Override
    public void updateNCharacterStream(final String label, final Reader reader, final long length) throws SQLException {
        this.origin.updateNCharacterStream(label, reader, length);
    }
    @Override
    public void updateAsciiStream(final int index, final InputStream value, final long length) throws SQLException {
        this.origin.updateAsciiStream(index, value, length);
    }
    @Override
    public void updateBinaryStream(final int index, final InputStream value, final long length) throws SQLException {
        this.origin.updateBinaryStream(index, value, length);
    }
    @Override
    public void updateCharacterStream(final int index, final Reader value, final long length) throws SQLException {
        this.origin.updateCharacterStream(index, value, length);
    }
    @Override
    public void updateAsciiStream(final String label, final InputStream value, final long length) throws SQLException {
        this.origin.updateAsciiStream(label, value, length);
    }
    @Override
    public void updateBinaryStream(final String label, final InputStream value, final long length) throws SQLException {
        this.origin.updateBinaryStream(label, value, length);
    }
    @Override
    public void updateCharacterStream(final String label, final Reader reader, final long length) throws SQLException {
        this.origin.updateCharacterStream(label, reader, length);
    }
    @Override
    public void updateBlob(final int index, final InputStream inputStream, final long length) throws SQLException {
        this.origin.updateBlob(index, inputStream, length);
    }
    @Override
    public void updateBlob(final String label, final InputStream inputStream, final long length) throws SQLException {
        this.origin.updateBlob(label, inputStream, length);
    }
    @Override
    public void updateClob(final int index, final Reader reader, final long length) throws SQLException {
        this.origin.updateClob(index, reader, length);
    }
    @Override
    public void updateClob(final String label, final Reader reader, final long length) throws SQLException {
        this.origin.updateClob(label, reader, length);
    }
    @Override
    public void updateNClob(final int index, final Reader reader, final long length) throws SQLException {
        this.origin.updateNClob(index, reader, length);
    }
    @Override
    public void updateNClob(final String label, final Reader reader, final long length) throws SQLException {
        this.origin.updateNClob(label, reader, length);
    }
    @Override
    public void updateNCharacterStream(final int index, final Reader value) throws SQLException {
        this.origin.updateNCharacterStream(index, value);
    }
    @Override
    public void updateNCharacterStream(final String label, final Reader reader) throws SQLException {
        this.origin.updateNCharacterStream(label, reader);
    }
    @Override
    public void updateAsciiStream(final int index, final InputStream value) throws SQLException {
        this.origin.updateAsciiStream(index, value);
    }
    @Override
    public void updateBinaryStream(final int index, final InputStream value) throws SQLException {
        this.origin.updateBinaryStream(index, value);
    }
    @Override
    public void updateCharacterStream(final int index, final Reader value) throws SQLException {
        this.origin.updateCharacterStream(index, value);
    }
    @Override
    public void updateAsciiStream(final String label, final InputStream value) throws SQLException {
        this.origin.updateAsciiStream(label, value);
    }
    @Override
    public void updateBinaryStream(final String label, final InputStream value) throws SQLException {
        this.origin.updateBinaryStream(label, value);
    }
    @Override
    public void updateCharacterStream(final String label, final Reader reader) throws SQLException {
        this.origin.updateCharacterStream(label, reader);
    }
    @Override
    public void updateBlob(final int index, final InputStream inputStream) throws SQLException {
        this.origin.updateBlob(index, inputStream);
    }
    @Override
    public void updateBlob(final String label, final InputStream inputStream) throws SQLException {
        this.origin.updateBlob(label, inputStream);
    }
    @Override
    public void updateClob(final int index, final Reader reader) throws SQLException {
        this.origin.updateClob(index, reader);
    }
    @Override
    public void updateClob(final String label, final Reader reader) throws SQLException {
        this.origin.updateClob(label, reader);
    }
    @Override
    public void updateNClob(final int index, final Reader reader) throws SQLException {
        this.origin.updateNClob(index, reader);
    }
    @Override
    public void updateNClob(final String label, final Reader reader) throws SQLException {
        this.origin.updateNClob(label, reader);
    }
    @Override
    public <T> T getObject(final int index, final Class<T> type) throws SQLException {
        return this.origin.getObject(index, type);
    }
    @Override
    public <T> T getObject(final String label, final Class<T> type) throws SQLException {
        return this.origin.getObject(label, type);
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
