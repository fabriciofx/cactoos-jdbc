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
package com.github.fabriciofx.cactoos.jdbc;

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
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version Id
 * @since
 */
public final class LooseResultSet implements ResultSet {
    private final List<Map<String, Object>> rows;
    private final AtomicInteger position;

    public LooseResultSet(final List<Map<String, Object>> rws) {
        this.rows = rws;
        this.position = new AtomicInteger(-1);
    }

    @Override
    public boolean next() throws SQLException {
        return this.position.incrementAndGet() < this.rows.size();
    }

    @Override
    public void close() throws SQLException {
    }

    @Override
    public boolean wasNull() throws SQLException {
        return false;
    }

    @Override
    public String getString(final int columnIndex) throws SQLException {
        return (String) this.rows.get(
            this.position.get()
        ).values().toArray()[columnIndex - 1];
    }

    @Override
    public boolean getBoolean(final int columnIndex) throws SQLException {
        return (Boolean) this.rows.get(
            this.position.get()
        ).values().toArray()[columnIndex - 1];
    }

    @Override
    public byte getByte(final int columnIndex) throws SQLException {
        return (Byte) this.rows.get(
            this.position.get()
        ).values().toArray()[columnIndex - 1];
    }

    @Override
    public short getShort(final int columnIndex) throws SQLException {
        return (Short) this.rows.get(
            this.position.get()
        ).values().toArray()[columnIndex - 1];
    }

    @Override
    public int getInt(final int columnIndex) throws SQLException {
        return (Integer) this.rows.get(
            this.position.get()
        ).values().toArray()[columnIndex - 1];
    }

    @Override
    public long getLong(final int columnIndex) throws SQLException {
        return (Long) this.rows.get(
            this.position.get()
        ).values().toArray()[columnIndex - 1];
    }

    @Override
    public float getFloat(final int columnIndex) throws SQLException {
        return (Float) this.rows.get(
            this.position.get()
        ).values().toArray()[columnIndex - 1];
    }

    @Override
    public double getDouble(final int columnIndex) throws SQLException {
        return (Double) this.rows.get(
            this.position.get()
        ).values().toArray()[columnIndex - 1];
    }

    @Override
    public byte[] getBytes(final int columnIndex) throws SQLException {
        return (byte[]) this.rows.get(
            this.position.get()
        ).values().toArray()[columnIndex - 1];
    }
    @Override
    public Date getDate(final int columnIndex) throws SQLException {
        return (Date) this.rows.get(
            this.position.get()
        ).values().toArray()[columnIndex - 1];
    }

    @Override
    public Time getTime(final int columnIndex) throws SQLException {
        return (Time) this.rows.get(
            this.position.get()
        ).values().toArray()[columnIndex - 1];
    }

    @Override
    public Timestamp getTimestamp(final int columnIndex) throws SQLException {
        return (Timestamp) this.rows.get(
            this.position.get()
        ).values().toArray()[columnIndex - 1];
    }

    @Override
    public String getString(final String columnLabel) throws SQLException {
        return (String) this.rows.get(this.position.get()).get(columnLabel);
    }

    @Override
    public boolean getBoolean(final String columnLabel) throws SQLException {
        return (Boolean) this.rows.get(this.position.get()).get(columnLabel);
    }

    @Override
    public byte getByte(final String columnLabel) throws SQLException {
        return (Byte) this.rows.get(this.position.get()).get(columnLabel);
    }

    @Override
    public short getShort(final String columnLabel) throws SQLException {
        return (Short) this.rows.get(this.position.get()).get(columnLabel);
    }

    @Override
    public int getInt(final String columnLabel) throws SQLException {
        return (Integer) this.rows.get(this.position.get()).get(columnLabel);
    }

    @Override
    public long getLong(final String columnLabel) throws SQLException {
        return (Long) this.rows.get(this.position.get()).get(columnLabel);
    }

    @Override
    public float getFloat(final String columnLabel) throws SQLException {
        return (Float) this.rows.get(this.position.get()).get(columnLabel);
    }

    @Override
    public double getDouble(final String columnLabel) throws SQLException {
        return (Double) this.rows.get(this.position.get()).get(columnLabel);
    }

    @Override
    public byte[] getBytes(final String columnLabel) throws SQLException {
        return (byte[]) this.rows.get(this.position.get()).get(columnLabel);
    }

    @Override
    public Date getDate(final String columnLabel) throws SQLException {
        return (Date) this.rows.get(this.position.get()).get(columnLabel);
    }

    @Override
    public Time getTime(final String columnLabel) throws SQLException {
        return (Time) this.rows.get(this.position.get()).get(columnLabel);
    }

    @Override
    public Timestamp getTimestamp(final String columnLabel) throws SQLException {
        return (Timestamp) this.rows.get(this.position.get()).get(columnLabel);
    }

    @Override
    public Object getObject(final int columnIndex) throws SQLException {
        return this.rows.get(
            this.position.get()
        ).values().toArray()[columnIndex - 1];
    }

    @Override
    public Object getObject(final String columnLabel) throws SQLException {
        return this.rows.get(this.position.get()).get(columnLabel);
    }

    @Override
    public int findColumn(final String columnLabel) throws SQLException {
        int col = 1;
        for (final String key : this.rows.get(0).keySet()) {
            if (key.equals(columnLabel.toLowerCase())) {
                break;
            }
            ++col;
        }
        return col;
    }

    @Override
    public BigDecimal getBigDecimal(final int columnIndex) throws SQLException {
        return (BigDecimal) this.rows.get(
            this.position.get()
        ).values().toArray()[columnIndex - 1];
    }

    @Override
    public BigDecimal getBigDecimal(
        final String columnLabel
    ) throws SQLException {
        return (BigDecimal) this.rows.get(this.position.get()).get(columnLabel);
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        return this.position.get() == -1;
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        return this.position.get() < this.rows.size();
    }

    @Override
    public boolean isFirst() throws SQLException {
        return this.position.get() == 0;
    }

    @Override
    public boolean isLast() throws SQLException {
        return this.position.get() == this.rows.size() - 1;
    }

    @Override
    public void beforeFirst() throws SQLException {
        this.position.set(-1);
    }

    @Override
    public void afterLast() throws SQLException {
        this.position.set(this.rows.size());
    }

    @Override
    public boolean first() throws SQLException {
        final boolean result;
        if (this.rows.isEmpty()) {
            result = false;
        } else {
            this.position.set(0);
            result = true;
        }
        return result;
    }

    @Override
    public boolean last() throws SQLException {
        final boolean result;
        if (this.rows.isEmpty()) {
            result = false;
        } else {
            this.position.set(this.rows.size() - 1);
            result = true;
        }
        return result;
    }

    @Override
    public int getRow() throws SQLException {
        return this.position.get() + 1;
    }

    @Override
    public boolean absolute(final int row) throws SQLException {
        this.position.set(row - 1);
        return true;
    }

    @Override
    public boolean relative(final int rows) throws SQLException {
        this.position.set(this.position.get() + rows);
        return true;
    }

    @Override
    public boolean previous() throws SQLException {
        this.position.decrementAndGet();
        return true;
    }

    @Override
    public Date getDate(
        final int columnIndex,
        final Calendar cal
    ) throws SQLException {
        cal.setTime(this.getDate(columnIndex));
        return new java.sql.Date(cal.getTimeInMillis());
    }

    @Override
    public Date getDate(
        final String columnLabel,
        final Calendar cal
    ) throws SQLException {
        cal.setTime(this.getDate(columnLabel));
        return new java.sql.Date(cal.getTimeInMillis());
    }

    @Override
    public Time getTime(
        final int columnIndex,
        final Calendar cal
    ) throws SQLException {
        cal.setTime(this.getTime(columnIndex));
        return new java.sql.Time(cal.getTimeInMillis());
    }

    @Override
    public Time getTime(
        final String columnLabel,
        final Calendar cal
    ) throws SQLException {
        cal.setTime(this.getTime(columnLabel));
        return new java.sql.Time(cal.getTimeInMillis());
    }

    @Override
    public Timestamp getTimestamp(
        final int columnIndex,
        final Calendar cal
    ) throws SQLException {
        cal.setTime(this.getTimestamp(columnIndex));
        return new java.sql.Timestamp(cal.getTimeInMillis());
    }

    @Override
    public Timestamp getTimestamp(
        final String columnLabel,
        final Calendar cal
    ) throws SQLException {
        cal.setTime(this.getTimestamp(columnLabel));
        return new java.sql.Timestamp(cal.getTimeInMillis());
    }

    @Override
    public URL getURL(final int columnIndex) throws SQLException {
        return (URL) this.rows.get(
            this.position.get()
        ).values().toArray()[columnIndex - 1];
    }

    @Override
    public URL getURL(final String columnLabel) throws SQLException {
        return (URL) this.rows.get(this.position.get()).get(columnLabel);
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public String getNString(final int columnIndex) throws SQLException {
        return this.getString(columnIndex);
    }

    @Override
    public String getNString(final String columnLabel) throws SQLException {
        return this.getString(columnLabel);
    }

    @Override
    public <T> T getObject(
        final int columnIndex,
        final Class<T> type
    ) throws SQLException {
        return type.cast(
            this.rows.get(
                this.position.get()
            ).values().toArray()[columnIndex - 1]
        );
    }

    @Override
    public <T> T getObject(
        final String columnLabel,
        final Class<T> type
    ) throws SQLException {
        return type.cast(this.rows.get(this.position.get()).get(columnLabel));
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return new LooseResultSetMetaData(this.rows);
    }

    @Deprecated
    public BigDecimal getBigDecimal(
        final int columnIndex,
        final int scale
    ) throws SQLException {
        throw new UnsupportedOperationException("#getBigDecimal()");
    }

    @Deprecated
    public BigDecimal getBigDecimal(
        final String columnLabel,
        final int scale
    ) throws SQLException {
        throw new UnsupportedOperationException("#getBigDecimal()");
    }

    @Override
    public InputStream getAsciiStream(
        final int columnIndex
    ) throws SQLException {
        throw new UnsupportedOperationException("#getAsciiStream()");
    }

    @Deprecated
    public InputStream getUnicodeStream(
        final int columnIndex
    ) throws SQLException {
        throw new UnsupportedOperationException("#getUnicodeStream()");
    }

    @Override
    public InputStream getBinaryStream(
        final int columnIndex
    ) throws SQLException {
        throw new UnsupportedOperationException("#getBinaryStream()");
    }

    @Override
    public InputStream getAsciiStream(
        final String columnLabel
    ) throws SQLException {
        throw new UnsupportedOperationException("#getAsciiStream()");
    }

    @Deprecated
    public InputStream getUnicodeStream(
        final String columnLabel
    ) throws SQLException {
        throw new UnsupportedOperationException("#getUnicodeStream()");
    }

    @Override
    public InputStream getBinaryStream(
        final String columnLabel
    ) throws SQLException {
        throw new UnsupportedOperationException("#getBinaryStream()");
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        throw new UnsupportedOperationException("#getWarnings()");
    }

    @Override
    public void clearWarnings() throws SQLException {
        throw new UnsupportedOperationException("#clearWarnings()");
    }

    @Override
    public String getCursorName() throws SQLException {
        throw new UnsupportedOperationException("#getCursorName()");
    }

    @Override
    public Reader getCharacterStream(
        final int columnIndex
    ) throws SQLException {
        throw new UnsupportedOperationException("#getCharacterStream()");
    }

    @Override
    public Reader getCharacterStream(
        final String columnLabel
    ) throws SQLException {
        throw new UnsupportedOperationException("#getCharacterStream()");
    }

    @Override
    public void setFetchDirection(final int direction) throws SQLException {
        throw new UnsupportedOperationException("#setFetchDirection()");
    }

    @Override
    public int getFetchDirection() throws SQLException {
        throw new UnsupportedOperationException("#getFetchDirection()");
    }

    @Override
    public void setFetchSize(final int rows) throws SQLException {
        throw new UnsupportedOperationException("#setFetchSize()");
    }

    @Override
    public int getFetchSize() throws SQLException {
        throw new UnsupportedOperationException("#getFetchSize()");
    }

    @Override
    public int getType() throws SQLException {
        throw new UnsupportedOperationException("#getType()");
    }

    @Override
    public int getConcurrency() throws SQLException {
        throw new UnsupportedOperationException("#getConcurrency()");
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        throw new UnsupportedOperationException("#rowUpdated()");
    }

    @Override
    public boolean rowInserted() throws SQLException {
        throw new UnsupportedOperationException("#rowInserted()");
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        throw new UnsupportedOperationException("#rowDeleted()");
    }

    @Override
    public void updateNull(final int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("#updateNull()");
    }

    @Override
    public void updateBoolean(
        final int columnIndex,
        final boolean x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateBoolean()");
    }

    @Override
    public void updateByte(
        final int columnIndex,
        final byte x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateByte()");
    }

    @Override
    public void updateShort(
        final int columnIndex,
        final short x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateShort()");
    }

    @Override
    public void updateInt(
        final int columnIndex,
        final int x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateInt()");
    }

    @Override
    public void updateLong(
        final int columnIndex,
        final long x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateLong()");
    }

    @Override
    public void updateFloat(
        final int columnIndex,
        final float x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateFloat()");
    }

    @Override
    public void updateDouble(
        final int columnIndex,
        final double x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateDouble()");
    }

    @Override
    public void updateBigDecimal(
        final int columnIndex,
        final BigDecimal x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateBigDecimal()");
    }

    @Override
    public void updateString(
        final int columnIndex,
        final String x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateString()");
    }

    @Override
    public void updateBytes(
        final int columnIndex,
        final byte[] x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateBytes()");
    }

    @Override
    public void updateDate(
        final int columnIndex,
        final Date x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateDate()");
    }

    @Override
    public void updateTime(
        final int columnIndex,
        final Time x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateTime()");
    }

    @Override
    public void updateTimestamp(
        final int columnIndex,
        final Timestamp x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateTimestamp()");
    }

    @Override
    public void updateAsciiStream(
        final int columnIndex,
        final InputStream x,
        final int length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateAsciiStream()");
    }

    @Override
    public void updateBinaryStream(
        final int columnIndex,
        final InputStream x,
        final int length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateBinaryStream()");
    }

    @Override
    public void updateCharacterStream(
        final int columnIndex,
        final Reader x,
        final int length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateCharacterStream()");
    }

    @Override
    public void updateObject(
        final int columnIndex,
        final Object x,
        final int scaleOrLength
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateObject()");
    }

    @Override
    public void updateObject(
        final int columnIndex,
        final Object x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateObject()");
    }

    @Override
    public void updateNull(final String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("#updateNull()");
    }

    @Override
    public void updateBoolean(
        final String columnLabel,
        final boolean x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateBoolean()");
    }

    @Override
    public void updateByte(
        final String columnLabel,
        final byte x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateByte()");
    }

    @Override
    public void updateShort(
        final String columnLabel,
        final short x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateShort()");
    }

    @Override
    public void updateInt(
        final String columnLabel,
        final int x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateInt()");
    }

    @Override
    public void updateLong(
        final String columnLabel,
        final long x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateLong()");
    }

    @Override
    public void updateFloat(
        final String columnLabel,
        final float x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateFloat()");
    }

    @Override
    public void updateDouble(
        final String columnLabel,
        final double x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateDouble()");
    }

    @Override
    public void updateBigDecimal(
        final String columnLabel,
        final BigDecimal x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateBigDecimal()");
    }

    @Override
    public void updateString(
        final String columnLabel,
        final String x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateString()");
    }

    @Override
    public void updateBytes(
        final String columnLabel,
        final byte[] x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateBytes()");
    }

    @Override
    public void updateDate(
        final String columnLabel,
        final Date x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateDate()");
    }

    @Override
    public void updateTime(
        final String columnLabel,
        final Time x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateTime()");
    }

    @Override
    public void updateTimestamp(
        final String columnLabel,
        final Timestamp x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateTimestamp()");
    }

    @Override
    public void updateAsciiStream(
        final String columnLabel,
        final InputStream x,
        final int length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateAsciiStream()");
    }

    @Override
    public void updateBinaryStream(
        final String columnLabel,
        final InputStream x,
        final int length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateBinaryStream()");
    }

    @Override
    public void updateCharacterStream(
        final String columnLabel,
        final Reader reader,
        final int length
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateCharacterStream()");
    }

    @Override
    public void updateObject(
        final String columnLabel,
        final Object x,
        final int scaleOrLength
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateObject()");
    }

    @Override
    public void updateObject(
        final String columnLabel,
        final Object x
    ) throws SQLException {
        throw new UnsupportedOperationException("#updateObject()");
    }

    @Override
    public void insertRow() throws SQLException {
        throw new UnsupportedOperationException("#insertRow()");
    }

    @Override
    public void updateRow() throws SQLException {
        throw new UnsupportedOperationException("#updateRow()");
    }

    @Override
    public void deleteRow() throws SQLException {
        throw new UnsupportedOperationException("#deleteRow()");
    }

    @Override
    public void refreshRow() throws SQLException {
        throw new UnsupportedOperationException("#refreshRow()");
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        throw new UnsupportedOperationException("#cancelRowUpdates()");
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        throw new UnsupportedOperationException("#moveToInsertRow()");
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        throw new UnsupportedOperationException("#moveToCurrentRow()");
    }

    @Override
    public Statement getStatement() throws SQLException {
        throw new UnsupportedOperationException("#getStatement()");
    }

    @Override
    public Object getObject(
        final int columnIndex,
        final Map<String, Class<?>> map
    ) throws SQLException {
        throw new UnsupportedOperationException("#getObject()");
    }

    @Override
    public Ref getRef(final int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("#getRef()");
    }

    @Override
    public Blob getBlob(final int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("#getBlob()");
    }

    @Override
    public Clob getClob(final int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("#getClob()");
    }

    @Override
    public Array getArray(final int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("#getArray()");
    }

    @Override
    public Object getObject(
        final String columnLabel,
        final Map<String, Class<?>> map
    ) throws SQLException {
        throw new UnsupportedOperationException("#getObject()");
    }

    @Override
    public Ref getRef(final String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("#getRef()");
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
