/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

import java.io.IOException;
import java.sql.PreparedStatement;

/**
 * Session.
 * @since 0.9.0
 */
public interface Session extends AutoCloseable {
    /**
     * Create a PreparedStatement from plan.
     * @param plan A Plan
     * @return A PreparedStatement
     * @throws Exception if something goes wrong
     */
    PreparedStatement prepared(Plan plan) throws Exception;

    /**
     * Turn on or off auto commit.
     * @param enabled If true, enable auto commit. If false, disable
     * @throws Exception if something goes wrong
     */
    void autoCommit(boolean enabled) throws Exception;

    /**
     * Commit a session.
     * @throws Exception if something goes wrong
     */
    void commit() throws Exception;

    /**
     * Rollback a session.
     * @throws Exception if something goes wrong
     */
    void rollback() throws Exception;

    @Override
    void close() throws IOException;
}
