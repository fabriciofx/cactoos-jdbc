/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.source;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Source;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.FormattedText;
import org.cactoos.text.UncheckedText;

/**
 * Logged Source.
 *
 * @since 0.1
 */
public final class Logged implements Source {
    /**
     * Source.
     */
    private final Source origin;

    /**
     * Where the logs come from.
     */
    private final String from;

    /**
     * The logger.
     */
    private final Logger logger;

    /**
     * The logger level.
     */
    private final Unchecked<Level> level;

    /**
     * The sessions counter.
     */
    private final AtomicInteger sessions;

    /**
     * Ctor.
     * @param source A Source
     * @param from Where the logs come from
     */
    public Logged(final Source source, final String from) {
        this(source, from, Logger.getLogger(from));
    }

    /**
     * Ctor.
     * @param source A Source
     * @param from Where the logs come from
     * @param logger A logger
     */
    public Logged(
        final Source source,
        final String from,
        final Logger logger
    ) {
        this.origin = source;
        this.from = from;
        this.logger = logger;
        this.level = new Unchecked<>(
            new Sticky<>(
                () -> {
                    Level lvl = logger.getLevel();
                    if (lvl == null) {
                        Logger parent = logger;
                        while (lvl == null) {
                            parent = parent.getParent();
                            lvl = parent.getLevel();
                        }
                    }
                    return lvl;
                }
            )
        );
        this.sessions = new AtomicInteger(-1);
    }

    @Override
    public Session session() throws Exception {
        final Session session = new com.github.fabriciofx.cactoos.jdbc.session.Logged(
            this.origin.session(),
            this.from,
            this.logger,
            this.level.value(),
            this.sessions.incrementAndGet()
        );
        this.logger.log(
            this.level.value(),
            new FormattedText(
                "[%s] Session[#%d] opened",
                this.from,
                this.sessions.get()
            ).asString()
        );
        return session;
    }

    @Override
    public String url() throws Exception {
        final String location = this.origin.url();
        this.logger.log(
            this.level.value(),
            new FormattedText(
                "[%s] Source retrieve url: '%s'",
                this.from,
                location
            ).asString()
        );
        return location;
    }

    @Override
    public String username() {
        final String user = this.origin.username();
        this.logger.log(
            this.level.value(),
            new UncheckedText(
                new FormattedText(
                    "[%s] Source retrieve username: '%s'",
                    this.from,
                    user
                )
            ).asString()
        );
        return user;
    }

    @Override
    public String password() {
        this.logger.log(
            this.level.value(),
            new UncheckedText(
                new FormattedText(
                    "[%s] Source retrieve password: '********'",
                    this.from
                )
            ).asString()
        );
        return this.origin.password();
    }
}
