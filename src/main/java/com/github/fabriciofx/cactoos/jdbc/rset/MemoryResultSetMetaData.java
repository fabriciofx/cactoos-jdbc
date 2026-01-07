/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.rset;

import com.github.fabriciofx.cactoos.jdbc.Columns;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * MemoryResultSetMetaData.
 *
 * An immutable, disconnected, in memory {@link ResultSetMetaData}.
 * @since 0.9.0
 * @checkstyle IllegalCatchCheck (200 lines)
 */
@SuppressWarnings("PMD.AvoidCatchingGenericException")
public final class MemoryResultSetMetaData implements ResultSetMetaData {
    /**
     * Columns.
     */
    private final Columns columns;

    /**
     * Ctor.
     * @param columns The columns
     */
    public MemoryResultSetMetaData(final Columns columns) {
        this.columns = columns;
    }

    @Override
    public int getColumnCount() throws SQLException {
        return this.columns.count();
    }

    @Override
    public boolean isAutoIncrement(final int column) throws SQLException {
        throw new UnsupportedOperationException(
            "#isAutoIncrement(int): read-only in memory ResultSetMetaData"
        );
    }

    @Override
    public boolean isCaseSensitive(final int column) throws SQLException {
        throw new UnsupportedOperationException(
            "#isCaseSensitive(int): read-only in memory ResultSetMetaData"
        );
    }

    @Override
    public boolean isSearchable(final int column) throws SQLException {
        throw new UnsupportedOperationException(
            "#isSearchable(int): read-only in memory ResultSetMetaData"
        );
    }

    @Override
    public boolean isCurrency(final int column) throws SQLException {
        throw new UnsupportedOperationException(
            "#isCurrency(int): read-only in memory ResultSetMetaData"
        );
    }

    @Override
    public int isNullable(final int column) throws SQLException {
        throw new UnsupportedOperationException(
            "#isNullable(int): read-only in memory ResultSetMetaData"
        );
    }

    @Override
    public boolean isSigned(final int column) throws SQLException {
        throw new UnsupportedOperationException(
            "#isSigned(int): read-only in memory ResultSetMetaData"
        );
    }

    @Override
    public int getColumnDisplaySize(final int column) throws SQLException {
        throw new UnsupportedOperationException(
            "#getColumnDisplaySize(int): read-only in memory ResultSetMetaData"
        );
    }

    @Override
    public String getColumnLabel(final int column) throws SQLException {
        try {
            return this.columns.name(column);
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public String getColumnName(final int column) throws SQLException {
        try {
            return this.columns.name(column);
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public String getSchemaName(final int column) throws SQLException {
        throw new UnsupportedOperationException(
            "#getSchemaName(int): read-only in memory ResultSetMetaData"
        );
    }

    @Override
    public int getPrecision(final int column) throws SQLException {
        throw new UnsupportedOperationException(
            "#getPrecision(int): read-only in memory ResultSetMetaData"
        );
    }

    @Override
    public int getScale(final int column) throws SQLException {
        throw new UnsupportedOperationException(
            "#getScale(int): read-only in memory ResultSetMetaData"
        );
    }

    @Override
    public String getTableName(final int column) throws SQLException {
        throw new UnsupportedOperationException(
            "#getTableName(int): read-only in memory ResultSetMetaData"
        );
    }

    @Override
    public String getCatalogName(final int column) throws SQLException {
        throw new UnsupportedOperationException(
            "#getCatalogName(int): read-only in memory ResultSetMetaData"
        );
    }

    @Override
    public int getColumnType(final int column) throws SQLException {
        try {
            return this.columns.type(this.columns.name(column));
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public String getColumnTypeName(final int column) throws SQLException {
        throw new UnsupportedOperationException(
            "#getColumnTypeName(int): read-only in memory ResultSetMetaData"
        );
    }

    @Override
    public boolean isReadOnly(final int column) throws SQLException {
        return true;
    }

    @Override
    public boolean isWritable(final int column) throws SQLException {
        return false;
    }

    @Override
    public boolean isDefinitelyWritable(final int column) throws SQLException {
        return false;
    }

    @Override
    public String getColumnClassName(final int column) throws SQLException {
        throw new UnsupportedOperationException(
            "#getColumnClassName(int): read-only in memory ResultSetMetaData"
        );
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
}
