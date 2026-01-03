/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.Session;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;

/**
 * Logged session.
 *
 * @since 0.1
 */
public final class Logged implements Session {
    /**
     * The session.
     */
    private final Session origin;

    /**
     * Where the data comes from.
     */
    private final String source;

    /**
     * The logger.
     */
    private final Logger logger;

    /**
     * The logger level.
     */
    private final Unchecked<Level> level;

    /**
     * The statements id.
     */
    private final AtomicInteger statements;

    /**
     * Ctor.
     * @param session A Session
     * @param source Where the data comes from
     */
    public Logged(final Session session, final String source) {
        this(session, source, Logger.getLogger(source));
    }

    /**
     * Ctor.
     * @param session A Session
     * @param src Where the data comes from
     * @param lgr A logger
     */
    public Logged(
        final Session session,
        final String src,
        final Logger lgr
    ) {
        this.origin = session;
        this.source = src;
        this.logger = lgr;
        this.level = new Unchecked<>(
            new Sticky<>(
                () -> {
                    Level lvl = lgr.getLevel();
                    if (lvl == null) {
                        Logger parent = lgr;
                        while (lvl == null) {
                            parent = parent.getParent();
                            lvl = parent.getLevel();
                        }
                    }
                    return lvl;
                }
            )
        );
        this.statements = new AtomicInteger();
    }

    @Override
    public Connexio connexio() throws Exception {
        return new com.github.fabriciofx.cactoos.jdbc.connexio.Logged(
            this.origin.connexio(),
            this.source,
            this.logger,
            this.level.value(),
            this.statements
        );
    }

    @Override
    public String url() throws Exception {
        return this.origin.url();
    }

    @Override
    public String username() {
        return this.origin.username();
    }

    @Override
    public String password() {
        return this.origin.password();
    }
}
