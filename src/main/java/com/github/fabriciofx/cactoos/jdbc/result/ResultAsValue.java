/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.result;

import com.github.fabriciofx.cactoos.jdbc.Statement;
import org.cactoos.Scalar;

/**
 * Result as data.
 *
 * @param <T> Type of the result
 * @since 0.3
 */
public final class ResultAsValue<T> implements Scalar<T> {
    /**
     * Statement that returns a ResultSet.
     */
    private final Statement<T> statement;

    /**
     * Ctor.
     * @param stmt A statement
     */
    public ResultAsValue(final Statement<T> stmt) {
        this.statement = stmt;
    }

    @Override
    public T value() throws Exception {
        return this.statement.execute();
    }
}
