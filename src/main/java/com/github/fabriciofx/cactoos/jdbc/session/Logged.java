/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Plan;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.query.Merged;
import com.github.fabriciofx.cactoos.jdbc.query.Named;
import com.github.fabriciofx.cactoos.jdbc.query.Normalized;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cactoos.text.FormattedText;
import org.cactoos.text.UncheckedText;

/**
 * Logged. A decorator for session that logged all activities.
 *
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
     * The session id.
     */
    private final int id;

    /**
     * Statements counter.
     */
    private final AtomicInteger statements;

    /**
     * Ctor.
     *
     * @param session A Session
     * @param from Where the logs come from
     * @param logger A logger
     * @param level A level
     * @param id Session id
     * @param statements Statements counter
     */
    public Logged(
        final Session session,
        final String from,
        final Logger logger,
        final Level level,
        final int id,
        final AtomicInteger statements
    ) {
        this.origin = session;
        this.from = from;
        this.logger = logger;
        this.level = level;
        this.id = id;
        this.statements = statements;
    }

    @Override
    public PreparedStatement prepared(final Plan plan) throws Exception {
        final PreparedStatement prepared = new com.github.fabriciofx.cactoos.jdbc.prepared.Logged(
            this.origin.prepared(plan),
            this.from,
            this.logger,
            this.level,
            this.statements.incrementAndGet()
        );
        this.logger.log(
            this.level,
            new FormattedText(
                "[%s] PreparedStatement[#%d] created using query: '%s'.\n    Stored query: '%s'\n       Cache key: '%s'",
                this.from,
                this.statements.get(),
                plan.query().sql(),
                new Normalized(plan.query()).sql(),
                new Normalized(new Merged(new Named(plan.query()))).sql()
            ).asString()
        );
        return prepared;
    }

    @Override
    public void autocommit(final boolean enabled) throws Exception {
        this.origin.autocommit(enabled);
        final String msg;
        if (enabled) {
            msg = "[%s] Session[#%d] autocommit enabled.";
        } else {
            msg = "[%s] Session[#%d] autocommit disabled.";
        }
        this.logger.log(
            this.level,
            new FormattedText(msg, this.from, this.id).asString()
        );
    }

    @Override
    public void commit() throws Exception {
        this.origin.commit();
        this.logger.log(
            this.level,
            new FormattedText(
                "[%s] Session[#%d] commit.",
                this.from,
                this.id
            ).asString()
        );
    }

    @Override
    public void rollback() throws Exception {
        this.origin.rollback();
        this.logger.log(
            this.level,
            new FormattedText(
                "[%s] Session[#%d] rollback.",
                this.from,
                this.id
            ).asString()
        );
    }

    @Override
    public void close() throws IOException {
        this.origin.close();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Session[#%d] closing.",
                    this.from,
                    this.id
                )
            ).asString()
        );
    }
}
