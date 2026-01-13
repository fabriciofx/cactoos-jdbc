/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.plan;

import com.github.fabriciofx.cactoos.jdbc.Plan;
import com.github.fabriciofx.cactoos.jdbc.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logged.
 * @since 0.9.0
 * @checkstyle ParameterNumberCheck (200 lines)
 */
public final class Logged implements Plan {
    /**
     * Plan.
     */
    private final Plan origin;

    /**
     * Where the logs come from.
     */
    private final String from;

    /**
     * Logger.
     */
    private final Logger logger;

    /**
     * Level.
     */
    private final Level level;

    /**
     * Connections id.
     */
    private final int connections;

    /**
     * The statements counter.
     */
    private final AtomicInteger statements;

    /**
     * Ctor.
     *
     * @param plan A Plan
     * @param from Where the logs come from
     * @param logger A logger
     * @param level A level
     * @param connections Connection id
     * @param statements Statements counter
     */
    public Logged(
        final Plan plan,
        final String from,
        final Logger logger,
        final Level level,
        final int connections,
        final AtomicInteger statements
    ) {
        this.origin = plan;
        this.from = from;
        this.logger = logger;
        this.level = level;
        this.connections = connections;
        this.statements = statements;
    }

    @Override
    public PreparedStatement prepare(final Connection connection)
        throws Exception {
        return this.origin.prepare(
            new com.github.fabriciofx.cactoos.jdbc.connection.Logged(
                connection,
                this.from,
                this.logger,
                this.level,
                this.connections,
                this.statements
            )
        );
    }

    @Override
    public Query query() {
        return this.origin.query();
    }
}
