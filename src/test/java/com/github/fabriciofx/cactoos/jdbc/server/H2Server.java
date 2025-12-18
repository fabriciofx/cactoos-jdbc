/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.server;

import com.github.fabriciofx.cactoos.jdbc.RandomDatabaseName;
import com.github.fabriciofx.cactoos.jdbc.Server;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.script.EmptyScript;
import com.github.fabriciofx.cactoos.jdbc.script.ScriptOf;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuth;
import com.github.fabriciofx.cactoos.jdbc.source.H2Source;
import com.github.fabriciofx.cactoos.jdbc.statement.Insert;
import java.io.IOException;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;

/**
 * MySQL server, for unit testing.
 *
 * @since 0.2
 * @checkstyle IllegalCatchCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidCatchingGenericException")
public final class H2Server implements Server {
    /**
     * The Database name.
     */
    private final Unchecked<String> dbname;

    /**
     * SQL Script to initialize the database.
     */
    private final ScriptOf script;

    /**
     * Ctor.
     */
    public H2Server() {
        this(new EmptyScript());
    }

    /**
     * Ctor.
     * @param text SqlScript to initialize the database.
     */
    public H2Server(final ScriptOf text) {
        this.dbname = new Unchecked<>(
            new Sticky<>(
                () -> new RandomDatabaseName().asString()
            )
        );
        this.script = text;
    }

    @Override
    public void start() throws Exception {
        this.script.run(this.session());
    }

    @Override
    public void stop() throws Exception {
        new Insert(this.session(), new QueryOf("SHUTDOWN")).result();
    }

    @Override
    public Session session() {
        return new NoAuth(
            new H2Source(this.dbname.value())
        );
    }

    @Override
    public void close() throws IOException {
        try {
            this.stop();
        } catch (final Exception ex) {
            throw new IOException(ex);
        }
    }
}
