/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Plan.
 * Prepares a {@link PreparedStatement} from a {@link Connection}.
 * @since 0.9.0
 */
public interface Plan {
    /**
     * Prepares a {@link PreparedStatement}.
     * @param connection A {@link Connection}
     * @return A {@link PreparedStatement}
     * @throws Exception if something goes wrong
     */
    PreparedStatement prepare(Connection connection) throws Exception;

    /**
     * Return the Plan's query.
     * @return A {@link Query}
     */
    Query query();
}
