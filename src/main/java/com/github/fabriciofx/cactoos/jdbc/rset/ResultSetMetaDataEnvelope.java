/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.rset;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * ResultSetMetadata Envelope.
 *
 * @since 0.4
 * @checkstyle DesignForExtensionCheck (1500 lines)
 */
public abstract class ResultSetMetaDataEnvelope implements ResultSetMetaData {
    /**
     * ResultSetMetaData to be decorated.
     */
    private final ResultSetMetaData origin;

    /**
     * Ctor.
     * @param rsmd Original ResultSetMetaData to be decorated
     */
    public ResultSetMetaDataEnvelope(final ResultSetMetaData rsmd) {
        this.origin = rsmd;
    }

    @Override
    public int getColumnCount() throws SQLException {
        return this.origin.getColumnCount();
    }

    @Override
    public boolean isAutoIncrement(final int column) throws SQLException {
        return this.origin.isAutoIncrement(column);
    }

    @Override
    public boolean isCaseSensitive(final int column) throws SQLException {
        return this.origin.isCaseSensitive(column);
    }

    @Override
    public boolean isSearchable(final int column) throws SQLException {
        return this.origin.isSearchable(column);
    }

    @Override
    public boolean isCurrency(final int column) throws SQLException {
        return this.origin.isCurrency(column);
    }

    @Override
    public int isNullable(final int column) throws SQLException {
        return this.origin.isNullable(column);
    }

    @Override
    public boolean isSigned(final int column) throws SQLException {
        return this.origin.isSigned(column);
    }

    @Override
    public int getColumnDisplaySize(final int column) throws SQLException {
        return this.origin.getColumnDisplaySize(column);
    }

    @Override
    public String getColumnLabel(final int column) throws SQLException {
        return this.origin.getColumnLabel(column);
    }

    @Override
    public String getColumnName(final int column) throws SQLException {
        return this.origin.getColumnName(column);
    }

    @Override
    public String getSchemaName(final int column) throws SQLException {
        return this.origin.getSchemaName(column);
    }

    @Override
    public int getPrecision(final int column) throws SQLException {
        return this.origin.getPrecision(column);
    }

    @Override
    public int getScale(final int column) throws SQLException {
        return this.origin.getScale(column);
    }

    @Override
    public String getTableName(final int column) throws SQLException {
        return this.origin.getTableName(column);
    }

    @Override
    public String getCatalogName(final int column) throws SQLException {
        return this.origin.getCatalogName(column);
    }

    @Override
    public int getColumnType(final int column) throws SQLException {
        return this.origin.getColumnType(column);
    }

    @Override
    public String getColumnTypeName(final int column) throws SQLException {
        return this.origin.getColumnTypeName(column);
    }

    @Override
    public boolean isReadOnly(final int column) throws SQLException {
        return this.origin.isReadOnly(column);
    }

    @Override
    public boolean isWritable(final int column) throws SQLException {
        return this.origin.isWritable(column);
    }

    @Override
    public boolean isDefinitelyWritable(final int column) throws SQLException {
        return this.origin.isDefinitelyWritable(column);
    }

    @Override
    public String getColumnClassName(final int column) throws SQLException {
        return this.origin.getColumnClassName(column);
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
