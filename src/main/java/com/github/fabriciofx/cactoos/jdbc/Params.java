/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

import java.sql.PreparedStatement;

/**
 * Query Params.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 */
public interface Params extends Iterable<Param> {
    /**
     * Set the PreparedStatement with all query parameters.
     * @param stmt The PreparedStatement
     * @return The set PreparedStatement
     * @throws Exception If fails
     */
    PreparedStatement prepare(PreparedStatement stmt) throws Exception;

    /**
     * Check if Params contains a param at position.
     * @param name The name of the parameter
     * @param index The position of the parameter in the PreparedStatement
     * @return True if contains or false if don't
     */
    boolean contains(String name, int index);

    /**
     * Get the Param at the position index.
     * @param index The parameter position in Params
     * @return The Param
     */
    Param param(int index);
}
