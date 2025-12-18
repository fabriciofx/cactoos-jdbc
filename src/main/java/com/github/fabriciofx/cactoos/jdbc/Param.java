/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

import java.sql.PreparedStatement;
import org.cactoos.Text;

/**
 * A named data.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 */
public interface Param extends Text {
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
}
