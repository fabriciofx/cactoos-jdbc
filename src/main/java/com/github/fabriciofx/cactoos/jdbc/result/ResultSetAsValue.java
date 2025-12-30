/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.result;

import com.github.fabriciofx.cactoos.jdbc.Statement;
import java.sql.ResultSet;
import java.util.NoSuchElementException;
import org.cactoos.Scalar;

/**
 * Result as data.
 *
 * @param <T> Type of the result
 * @since 0.1
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class ResultSetAsValue<T> implements Scalar<T> {
    /**
     * Statement that returns a ResultSet.
     */
    private final Statement<ResultSet> statement;

    /**
     * Ctor.
     * @param stmt A statement
     */
    public ResultSetAsValue(final Statement<ResultSet> stmt) {
        this.statement = stmt;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T value() throws Exception {
        try (ResultSet rset = this.statement.execute()) {
            if (rset.next()) {
                return (T) rset.getObject(1);
            }
            throw new NoSuchElementException("value not found");
        }
    }
}
