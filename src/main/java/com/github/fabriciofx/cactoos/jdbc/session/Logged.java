/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Plan;
import com.github.fabriciofx.cactoos.jdbc.Session;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logged.
 * A decorator for session that logged all activities.
 * @since 0.9.0
 * @checkstyle ParameterNumberCheck (150 lines)
 */
public final class Logged implements Session {
    /**
     * Session.
     */
    private final Session origin;

    /**
     * Where the logs come from.
     */
    private final String source;

    /**
     * Logger.
     */
    private final Logger logger;

    /**
     * Level.
     */
    private final Level level;

    /**
     * Statements counter.
     */
    private final AtomicInteger statements;

    /**
     * Ctor.
     * @param session A Session
     * @param source The source, where the logs come from
     * @param logger A logger
     * @param level A level
     * @param statements Statements counter
     */
    public Logged(
        final Session session,
        final String source,
        final Logger logger,
        final Level level,
        final AtomicInteger statements
    ) {
        this.origin = session;
        this.source = source;
        this.logger = logger;
        this.level = level;
        this.statements = statements;
    }

    @Override
    public PreparedStatement prepared(final Plan plan) throws Exception {
        return new com.github.fabriciofx.cactoos.jdbc.prepared.Logged(
            this.origin.prepared(plan),
            this.source,
            this.logger,
            this.level,
            this.statements.getAndIncrement()
        );
    }

    @Override
    public void autocommit(final boolean enabled) throws Exception {
        this.origin.autocommit(enabled);
    }

    @Override
    public void commit() throws Exception {
        this.origin.commit();
    }

    @Override
    public void rollback() throws Exception {
        this.origin.rollback();
    }

    @Override
    public void close() throws IOException {
        this.origin.close();
    }
}
