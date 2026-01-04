/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
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
     * Where the data comes from.
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
     * The statements id.
     */
    private final AtomicInteger statements;

    /**
     * Ctor.
     * @param source A Source
     * @param from Where the data comes from
     */
    public Logged(final Source source, final String from) {
        this(source, from, Logger.getLogger(from));
    }

    /**
     * Ctor.
     * @param source A Source
     * @param from Where the data comes from
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
        this.statements = new AtomicInteger();
    }

    @Override
    public Session session() throws Exception {
        return new com.github.fabriciofx.cactoos.jdbc.session.Logged(
            this.origin.session(),
            this.from,
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
