/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

import java.sql.PreparedStatement;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParserPos;

/**
 * A named data.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 */
public interface Param {
    /**
     * Get the data name.
     * @return The name
     */
    String name();

    /**
     * Set the PreparedStatement with data.
     * @param stmt The PreparedStatement
     * @param index The position of the parameter in the PreparedStatement
     * @throws Exception If fails
     */
    void prepare(PreparedStatement stmt, int index) throws Exception;

    /**
     * Returns the value.
     * @param from From which point
     * @return The converted value
     */
    SqlNode value(SqlParserPos from);
}
