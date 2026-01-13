/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
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
     * Connection id.
     */
    private final int connection;

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
     * @param connection Connection id
     * @param statements Statements counter
     */
    public Logged(
        final Session session,
        final String from,
        final Logger logger,
        final Level level,
        final int id,
        final int connection,
        final AtomicInteger statements
    ) {
        this.origin = session;
        this.from = from;
        this.logger = logger;
        this.level = level;
        this.id = id;
        this.connection = connection;
        this.statements = statements;
    }

    @Override
    public PreparedStatement prepared(final Plan plan) throws Exception {
        final PreparedStatement prepared = this.origin.prepared(
            new com.github.fabriciofx.cactoos.jdbc.plan.Logged(
                plan,
                this.from,
                this.logger,
                this.level,
                this.connection,
                this.statements
            )
        );
        this.logger.log(
            this.level,
            new FormattedText(
                "[%s] Session[#%d] prepared PreparedStatement[#%d].",
                this.from,
                this.id,
                this.statements.get()
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
                "[%s] Session[#%d] commited.",
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
                "[%s] Session[#%d] rolled back.",
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
                    "[%s] Session[#%d] closed.",
                    this.from,
                    this.id
                )
            ).asString()
        );
    }
}
