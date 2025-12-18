/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.result;

import com.github.fabriciofx.cactoos.jdbc.Statement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.cactoos.Scalar;

/**
 * Result as values.
 *
 * @param <T> Type of the result
 * @since 0.1
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class ResultSetAsValues<T> implements Scalar<List<T>> {
    /**
     * Statement that returns a ResultSet.
     */
    private final Statement<ResultSet> statement;

    /**
     * Ctor.
     * @param stmt A statement that returns a Rows
     */
    public ResultSetAsValues(final Statement<ResultSet> stmt) {
        this.statement = stmt;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> value() throws Exception {
        final List<T> values = new LinkedList<>();
        try (ResultSet rset = this.statement.result()) {
            for (final Map<String, Object> row : new ResultSetAsRows(rset)) {
                for (final Object obj : row.values()) {
                    values.add((T) obj);
                }
            }
        }
        return values;
    }
}
