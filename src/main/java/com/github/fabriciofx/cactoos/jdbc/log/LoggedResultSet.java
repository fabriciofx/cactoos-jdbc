/**
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version Id
 * @since
 */
public final class LoggedResultSet implements ResultSet {
    private final ResultSet origin;
    private final String source;
    private final Logger logger;
    private final Level level;

    public LoggedResultSet(
        final ResultSet rset,
        final String src,
        final Logger lggr,
        final Level lvl
    ) {
        this.origin = rset;
        this.source = src;
        this.logger = lggr;
        this.level = lvl;
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
        this.origin.wasNull();
    }

    @Override
    public String getString(final int columnIndex) throws SQLException {
        return this.origin.getString(columnIndex);
    }

    @Override
    public boolean getBoolean(final int columnIndex) throws SQLException {
        return this.origin.getBoolean(columnIndex);
    }

    @Override
    public byte getByte(final int columnIndex) throws SQLException {
        return this.origin.getByte(columnIndex);
    }

    @Override
    public short getShort(final int columnIndex) throws SQLException {
        return this.origin.getShort(columnIndex);
    }

    @Override
    public int getInt(final int columnIndex) throws SQLException {
        return this.origin.getInt(columnIndex);
    }

    @Override
    public long getLong(final int columnIndex) throws SQLException {
        return this.origin.getLong(columnIndex);
    }

    @Override
    public float getFloat(final int columnIndex) throws SQLException {
        return this.origin.getFloat(columnIndex);
    }

    @Override
    public double getDouble(final int columnIndex) throws SQLException {
        return this.origin.getDouble(columnIndex);
    }

    @Override
    public byte[] getBytes(final int columnIndex) throws SQLException {
        return this.origin.getBytes(columnIndex);
    }

    @Override
    public Date getDate(final int columnIndex) throws SQLException {
        return this.origin.getDate(columnIndex);
    }

    @Override
    public Time getTime(final int columnIndex) throws SQLException {
        return this.origin.getTime(columnIndex);
    }

    @Override
    public Timestamp getTimestamp(final int columnIndex) throws SQLException {
    }

    @Override
    public String getString(final String columnLabel) throws SQLException {
    }

    @Override
    public boolean getBoolean(final String columnLabel) throws SQLException {
    }

    @Override
    public byte getByte(final String columnLabel) throws SQLException {
    }

    @Override
    public short getShort(final String columnLabel) throws SQLException {
    }

    @Override
    public int getInt(final String columnLabel) throws SQLException {
    }

    @Override
    public long getLong(final String columnLabel) throws SQLException {
    }

    @Override
    public float getFloat(final String columnLabel) throws SQLException {
    }

    @Override
    public double getDouble(final String columnLabel) throws SQLException {
    }

    @Override
    public byte[] getBytes(final String columnLabel) throws SQLException {
    }

    @Override
    public Date getDate(final String columnLabel) throws SQLException {
    }

    @Override
    public Time getTime(final String columnLabel) throws SQLException {
    }

    @Override
    public Timestamp getTimestamp(final String columnLabel) throws SQLException {
    }

    @Override
    public Object getObject(final int columnIndex) throws SQLException {
    }

    @Override
    public Object getObject(final String columnLabel) throws SQLException {
    }

    @Override
    public int findColumn(final String columnLabel) throws SQLException {
    }

    @Override
    public BigDecimal getBigDecimal(final int columnIndex) throws SQLException {
    }

    @Override
    public BigDecimal getBigDecimal(
        final String columnLabel
    ) throws SQLException {
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
    }

    @Override
    public boolean isAfterLast() throws SQLException {
    }

    @Override
    public boolean isFirst() throws SQLException {
    }

    @Override
    public boolean isLast() throws SQLException {
    }

    @Override
    public void beforeFirst() throws SQLException {
    }

    @Override
    public void afterLast() throws SQLException {
    }

    @Override
    public boolean first() throws SQLException {
    }

    @Override
    public boolean last() throws SQLException {
    }

    @Override
    public int getRow() throws SQLException {
    }

    @Override
    public boolean absolute(final int row) throws SQLException {
    }

    @Override
    public boolean relative(final int rows) throws SQLException {
    }

    @Override
    public boolean previous() throws SQLException {
    }

    @Override
    public Date getDate(
        final int columnIndex,
        final Calendar cal
    ) throws SQLException {
    }

    @Override
    public Date getDate(
        final String columnLabel,
        final Calendar cal
    ) throws SQLException {
    }

    @Override
    public Time getTime(
        final int columnIndex,
        final Calendar cal
    ) throws SQLException {
    }

    @Override
    public Time getTime(
        final String columnLabel,
        final Calendar cal
    ) throws SQLException {
    }

    @Override
    public Timestamp getTimestamp(
        final int columnIndex,
        final Calendar cal
    ) throws SQLException {
    }

    @Override
    public Timestamp getTimestamp(
        final String columnLabel,
        final Calendar cal
    ) throws SQLException {
    }

    @Override
    public URL getURL(final int columnIndex) throws SQLException {
    }

    @Override
    public URL getURL(final String columnLabel) throws SQLException {
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public String getNString(final int columnIndex) throws SQLException {
    }

    @Override
    public String getNString(final String columnLabel) throws SQLException {
    }

    @Override
    public <T> T getObject(
        final int columnIndex,
        final Class<T> type
    ) throws SQLException {
    }

    @Override
    public <T> T getObject(
        final String columnLabel,
        final Class<T> type
    ) throws SQLException {
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
    }

    @Deprecated
    public BigDecimal getBigDecimal(
        final int columnIndex,
        final int scale
    ) throws SQLException {
    }

    @Deprecated
    public BigDecimal getBigDecimal(
        final String columnLabel,
        final int scale
    ) throws SQLException {
    }

    @Override
    public InputStream getAsciiStream(
        final int columnIndex
    ) throws SQLException {
    }

    @Deprecated
    public InputStream getUnicodeStream(
        final int columnIndex
    ) throws SQLException {
    }

    @Override
    public InputStream getBinaryStream(
        final int columnIndex
    ) throws SQLException {
    }

    @Override
    public InputStream getAsciiStream(
        final String columnLabel
    ) throws SQLException {
    }

    @Deprecated
    public InputStream getUnicodeStream(
        final String columnLabel
    ) throws SQLException {
    }

    @Override
    public InputStream getBinaryStream(
        final String columnLabel
    ) throws SQLException {
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
    }

    @Override
    public void clearWarnings() throws SQLException {
    }

    @Override
    public String getCursorName() throws SQLException {
    }

    @Override
    public Reader getCharacterStream(
        final int columnIndex
    ) throws SQLException {
    }

    @Override
    public Reader getCharacterStream(
        final String columnLabel
    ) throws SQLException {
    }

    @Override
    public void setFetchDirection(final int direction) throws SQLException {
    }

    @Override
    public int getFetchDirection() throws SQLException {
    }

    @Override
    public void setFetchSize(final int rows) throws SQLException {
    }

    @Override
    public int getFetchSize() throws SQLException {
    }

    @Override
    public int getType() throws SQLException {
    }

    @Override
    public int getConcurrency() throws SQLException {
    }

    @Override
    public boolean rowUpdated() throws SQLException {
    }

    @Override
    public boolean rowInserted() throws SQLException {
    }

    @Override
    public boolean rowDeleted() throws SQLException {
    }

    @Override
    public void updateNull(final int columnIndex) throws SQLException {
    }

    @Override
    public void updateBoolean(
        final int columnIndex,
        final boolean x
    ) throws SQLException {
    }

    @Override
    public void updateByte(
        final int columnIndex,
        final byte x
    ) throws SQLException {
    }

    @Override
    public void updateShort(
        final int columnIndex,
        final short x
    ) throws SQLException {
    }

    @Override
    public void updateInt(
        final int columnIndex,
        final int x
    ) throws SQLException {
    }

    @Override
    public void updateLong(
        final int columnIndex,
        final long x
    ) throws SQLException {
    }

    @Override
    public void updateFloat(
        final int columnIndex,
        final float x
    ) throws SQLException {
    }

    @Override
    public void updateDouble(
        final int columnIndex,
        final double x
    ) throws SQLException {
    }

    @Override
    public void updateBigDecimal(
        final int columnIndex,
        final BigDecimal x
    ) throws SQLException {
    }

    @Override
    public void updateString(
        final int columnIndex,
        final String x
    ) throws SQLException {
    }

    @Override
    public void updateBytes(
        final int columnIndex,
        final byte[] x
    ) throws SQLException {
    }

    @Override
    public void updateDate(
        final int columnIndex,
        final Date x
    ) throws SQLException {
    }

    @Override
    public void updateTime(
        final int columnIndex,
        final Time x
    ) throws SQLException {
    }

    @Override
    public void updateTimestamp(
        final int columnIndex,
        final Timestamp x
    ) throws SQLException {
    }

    @Override
    public void updateAsciiStream(
        final int columnIndex,
        final InputStream x,
        final int length
    ) throws SQLException {
    }

    @Override
    public void updateBinaryStream(
        final int columnIndex,
        final InputStream x,
        final int length
    ) throws SQLException {
    }

    @Override
    public void updateCharacterStream(
        final int columnIndex,
        final Reader x,
        final int length
    ) throws SQLException {
    }

    @Override
    public void updateObject(
        final int columnIndex,
        final Object x,
        final int scaleOrLength
    ) throws SQLException {
    }

    @Override
    public void updateObject(
        final int columnIndex,
        final Object x
    ) throws SQLException {
    }

    @Override
    public void updateNull(final String columnLabel) throws SQLException {
    }

    @Override
    public void updateBoolean(
        final String columnLabel,
        final boolean x
    ) throws SQLException {
    }

    @Override
    public void updateByte(
        final String columnLabel,
        final byte x
    ) throws SQLException {
    }

    @Override
    public void updateShort(
        final String columnLabel,
        final short x
    ) throws SQLException {
    }

    @Override
    public void updateInt(
        final String columnLabel,
        final int x
    ) throws SQLException {
    }

    @Override
    public void updateLong(
        final String columnLabel,
        final long x
    ) throws SQLException {
    }

    @Override
    public void updateFloat(
        final String columnLabel,
        final float x
    ) throws SQLException {
    }

    @Override
    public void updateDouble(
        final String columnLabel,
        final double x
    ) throws SQLException {
    }

    @Override
    public void updateBigDecimal(
        final String columnLabel,
        final BigDecimal x
    ) throws SQLException {
    }

    @Override
    public void updateString(
        final String columnLabel,
        final String x
    ) throws SQLException {
    }

    @Override
    public void updateBytes(
        final String columnLabel,
        final byte[] x
    ) throws SQLException {
    }

    @Override
    public void updateDate(
        final String columnLabel,
        final Date x
    ) throws SQLException {
    }

    @Override
    public void updateTime(
        final String columnLabel,
        final Time x
    ) throws SQLException {
    }

    @Override
    public void updateTimestamp(
        final String columnLabel,
        final Timestamp x
    ) throws SQLException {
    }

    @Override
    public void updateAsciiStream(
        final String columnLabel,
        final InputStream x,
        final int length
    ) throws SQLException {
    }

    @Override
    public void updateBinaryStream(
        final String columnLabel,
        final InputStream x,
        final int length
    ) throws SQLException {
    }

    @Override
    public void updateCharacterStream(
        final String columnLabel,
        final Reader reader,
        final int length
    ) throws SQLException {
    }

    @Override
    public void updateObject(
        final String columnLabel,
        final Object x,
        final int scaleOrLength
    ) throws SQLException {
    }

    @Override
    public void updateObject(
        final String columnLabel,
        final Object x
    ) throws SQLException {
    }

    @Override
    public void insertRow() throws SQLException {
    }

    @Override
    public void updateRow() throws SQLException {
    }

    @Override
    public void deleteRow() throws SQLException {
    }

    @Override
    public void refreshRow() throws SQLException {
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
    }

    @Override
    public void moveToInsertRow() throws SQLException {
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
    }

    @Override
    public Statement getStatement() throws SQLException {
    }

    @Override
    public Object getObject(
        final int columnIndex,
        final Map<String, Class<?>> map
    ) throws SQLException {
    }

    @Override
    public Ref getRef(final int columnIndex) throws SQLException {
    }

    @Override
    public Blob getBlob(final int columnIndex) throws SQLException {
    }

    @Override
    public Clob getClob(final int columnIndex) throws SQLException {
    }

    @Override
    public Array getArray(final int columnIndex) throws SQLException {
    }

    @Override
    public Object getObject(
        final String columnLabel,
        final Map<String, Class<?>> map
    ) throws SQLException {
    }

    @Override
    public Ref getRef(final String columnLabel) throws SQLException {
    }

    @Override
    public Blob getBlob(final String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("#getBlob()");
    }

    @Override
    public Clob getClob(final String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("#getClob()");
    }

    @Override
    public Array getArray(final String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("#getArray()");
    }

    @Override
    public void updateRef(
        final int columnIndex,
        final Ref x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateRef()");
    }

    @Override
    public void updateRef(
        final String columnLabel,
        final Ref x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateRef()");
    }

    @Override
    public void updateBlob(
        final int columnIndex,
        final Blob x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateBlob()");
    }

    @Override
    public void updateBlob(
        final String columnLabel,
        final Blob x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateBlob()");
    }

    @Override
    public void updateClob(
        final int columnIndex,
        final Clob x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateClob()");
    }

    @Override
    public void updateClob(
        final String columnLabel,
        final Clob x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateClob()");
    }

    @Override
    public void updateArray(
        final int columnIndex,
        final Array x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateArray()");
    }

    @Override
    public void updateArray(
        final String columnLabel,
        final Array x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateArray()");
    }

    @Override
    public RowId getRowId(final int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("#getRowId()");
    }

    @Override
    public RowId getRowId(final String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("#getRowId()");
    }

    @Override
    public void updateRowId(
        final int columnIndex,
        final RowId x
    ) throws SQLException {
        throw new UnsupportedOperationException("#update()");
    }

    @Override
    public void updateRowId(
        final String columnLabel,
        final RowId x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateRowId()");
    }

    @Override
    public int getHoldability() throws SQLException {
        throw new UnsupportedOperationException("#getHoldability()");
    }

    @Override
    public void updateNString(
        final int columnIndex,
        final String nString
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateNString()");
    }

    @Override
    public void updateNString(
        final String columnLabel,
        final String nString
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateNString()");
    }

    @Override
    public void updateNClob(
        final int columnIndex,
        final NClob nClob
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateNClob()");
    }

    @Override
    public void updateNClob(
        final String columnLabel,
        final NClob nClob
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateNClob()");
    }

    @Override
    public NClob getNClob(final int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("#getNClob()");
    }

    @Override
    public NClob getNClob(final String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("#getNClob()");
    }

    @Override
    public SQLXML getSQLXML(final int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("#getSQLXML()");
    }

    @Override
    public SQLXML getSQLXML(final String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("#getSQLXML()");
    }

    @Override
    public void updateSQLXML(
        final int columnIndex,
        final SQLXML xmlObject
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateSQLXML()");
    }

    @Override
    public void updateSQLXML(
        final String columnLabel,
        final SQLXML xmlObject
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateSQLXML()");
    }

    @Override
    public Reader getNCharacterStream(
        final int columnIndex
    ) throws SQLException {
        throw new UnsupportedOperationException("#getNCharacterStream()");
    }

    @Override
    public Reader getNCharacterStream(
        final String columnLabel
    ) throws SQLException {
        throw new UnsupportedOperationException("#getNCharacterStream()");
    }

    @Override
    public void updateNCharacterStream(
        final int columnIndex,
        final Reader x,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateNCharacterStream()");
    }

    @Override
    public void updateNCharacterStream(
        final String columnLabel,
        final Reader reader,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateNCharacterStream()");
    }

    @Override
    public void updateAsciiStream(
        final int columnIndex,
        final InputStream x,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateAsciiStream()");
    }

    @Override
    public void updateBinaryStream(
        final int columnIndex,
        final InputStream x,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateBinaryStream()");
    }

    @Override
    public void updateCharacterStream(
        final int columnIndex,
        final Reader x,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateCharacterStream()");
    }

    @Override
    public void updateAsciiStream(
        final String columnLabel,
        final InputStream x,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateAsciiStream()");
    }

    @Override
    public void updateBinaryStream(
        final String columnLabel,
        final InputStream x,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateBinaryStream()");
    }

    @Override
    public void updateCharacterStream(
        final String columnLabel,
        final Reader reader,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateCharacterStream()");
    }

    @Override
    public void updateBlob(
        final int columnIndex,
        final InputStream inputStream,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateBlob()");
    }

    @Override
    public void updateBlob(
        final String columnLabel,
        final InputStream inputStream,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateBlob()");
    }

    @Override
    public void updateClob(
        final int columnIndex,
        final Reader reader,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateClob()");
    }

    @Override
    public void updateClob(
        final String columnLabel,
        final Reader reader,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateClob()");
    }

    @Override
    public void updateNClob(
        final int columnIndex,
        final Reader reader,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateNClob()");
    }

    @Override
    public void updateNClob(
        final String columnLabel,
        final Reader reader,
        final long length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateNClob()");
    }

    @Override
    public void updateNCharacterStream(
        final int columnIndex,
        final Reader x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateNCharacterStream()");
    }

    @Override
    public void updateNCharacterStream(
        final String columnLabel,
        final Reader reader
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateNCharacterStream()");
    }

    @Override
    public void updateAsciiStream(
        final int columnIndex,
        final InputStream x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateAsciiStream()");
    }

    @Override
    public void updateBinaryStream(
        final int columnIndex,
        final InputStream x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateBinaryStream()");
    }

    @Override
    public void updateCharacterStream(
        final int columnIndex,
        final Reader x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateCharacterStream()");
    }

    @Override
    public void updateAsciiStream(
        final String columnLabel,
        final InputStream x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateAsciiStream()");
    }

    @Override
    public void updateBinaryStream(
        final String columnLabel,
        final InputStream x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateBinaryStream()");
    }

    @Override
    public void updateCharacterStream(
        final String columnLabel,
        final Reader reader
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateNCharacterStream()");
    }

    @Override
    public void updateBlob(
        final int columnIndex,
        final InputStream inputStream
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateBlob()");
    }

    @Override
    public void updateBlob(
        final String columnLabel,
        final InputStream inputStream
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateBlob()");
    }

    @Override
    public void updateClob(
        final int columnIndex,
        final Reader reader
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateClob()");
    }

    @Override
    public void updateClob(
        final String columnLabel,
        final Reader reader
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateClob()");
    }

    @Override
    public void updateNClob(
        final int columnIndex,
        final Reader reader
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateNClob()");
    }

    @Override
    public void updateNClob(
        final String columnLabel,
        final Reader reader
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateNClob()");
    }

    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("#unwrap()");
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException("#isWrapperFor()");
    }
}
