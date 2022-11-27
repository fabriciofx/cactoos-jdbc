/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018-2022 Fabrício Barros Cabral
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.fabriciofx.cactoos.jdbc.server;

import com.github.fabriciofx.cactoos.jdbc.RandomDatabaseName;
import com.github.fabriciofx.cactoos.jdbc.Server;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.script.ScriptSql;
import com.github.fabriciofx.cactoos.jdbc.script.ScriptSqlEmpty;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuth;
import com.github.fabriciofx.cactoos.jdbc.source.H2Source;
import java.io.IOException;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;

/**
 * MySQL server, for unit testing.
 *
 * @since 0.2
 */
public final class H2Server implements Server {
    /**
     * The Database name.
     */
    private final Unchecked<String> dbname;

    /**
     * SQL Script to initialize the database.
     */
    private final ScriptSql script;

    /**
     * Ctor.
     */
    public H2Server() {
        this(new ScriptSqlEmpty());
    }

    /**
     * Ctor.
     * @param scrpt SqlScript to initialize the database.
     */
    public H2Server(final ScriptSql scrpt) {
        this.dbname = new Unchecked<>(
            new Sticky<>(
                () -> new RandomDatabaseName().asString()
            )
        );
        this.script = scrpt;
    }

    @Override
    public void start() throws Exception {
        this.script.run(this.session());
    }

    @Override
    public void stop() throws Exception {
        // Intended empty.
    }

    @Override
    public Session session() {
        return new NoAuth(
            new H2Source(this.dbname.value())
        );
    }

    @Override
    public void close() throws IOException {
        // Intended empty.
    }
}
