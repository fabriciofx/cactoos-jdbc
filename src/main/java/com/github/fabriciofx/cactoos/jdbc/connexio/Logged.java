/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.connexio;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.Query;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logged.
 * A decorator for connexio that logged all activities.
 * @since 0.9.0
 * @checkstyle ParameterNumberCheck (150 lines)
 */
public final class Logged implements Connexio {
    /**
     * Connexio.
     */
    private final Connexio origin;

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
     * @param connexio A Connexio
     * @param source The source, where the logs come from
     * @param logger A logger
     * @param level A level
     * @param statements Statements counter
     */
    public Logged(
        final Connexio connexio,
        final String source,
        final Logger logger,
        final Level level,
        final AtomicInteger statements
    ) {
        this.origin = connexio;
        this.source = source;
        this.logger = logger;
        this.level = level;
        this.statements = statements;
    }

    @Override
    public PreparedStatement prepared(final Query query) throws Exception {
        return new com.github.fabriciofx.cactoos.jdbc.jdk.prepared.Logged(
            this.origin.prepared(query),
            this.source,
            this.logger,
            this.level,
            this.statements.getAndIncrement()
        );
    }

    @Override
    public PreparedStatement batched(final Query query) throws Exception {
        return new com.github.fabriciofx.cactoos.jdbc.jdk.prepared.Logged(
            this.origin.batched(query),
            this.source,
            this.logger,
            this.level,
            this.statements.getAndIncrement()
        );
    }

    @Override
    public PreparedStatement keyed(final Query query) throws Exception {
        return new com.github.fabriciofx.cactoos.jdbc.jdk.prepared.Logged(
            this.origin.keyed(query),
            this.source,
            this.logger,
            this.level,
            this.statements.getAndIncrement()
        );
    }

    @Override
    public void autoCommit(final boolean enabled) throws Exception {
        this.origin.autoCommit(enabled);
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
