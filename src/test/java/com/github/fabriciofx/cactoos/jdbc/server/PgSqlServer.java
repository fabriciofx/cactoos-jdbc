/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018 Fabrício Barros Cabral
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
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
import com.github.fabriciofx.cactoos.jdbc.SqlScript;
import com.github.fabriciofx.cactoos.jdbc.query.SimpleQuery;
import com.github.fabriciofx.cactoos.jdbc.session.AuthSession;
import com.github.fabriciofx.cactoos.jdbc.source.PgSqlSource;
import com.github.fabriciofx.cactoos.jdbc.stmt.Update;
import org.cactoos.scalar.StickyScalar;
import org.cactoos.scalar.UncheckedScalar;
import org.cactoos.text.FormattedText;
import org.cactoos.text.JoinedText;
import org.cactoos.text.RandomText;

public final class PgSqlServer implements Server {
    private final UncheckedScalar<String> dbname;
    private final Session session;
    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final SqlScript script;

    public PgSqlServer() {
        this(SqlScript.NOP);
    }

    public PgSqlServer(final SqlScript scrpt) {
        this("localhost", 5432, "postgres", "postgres", scrpt);
    }

    public PgSqlServer(
        final String hst,
        final int prt,
        final String srnm,
        final String psswrd,
        final SqlScript scrpt
    ) {
        this.dbname = new UncheckedScalar<>(
            new StickyScalar<>(
                () -> new RandomDatabaseName().asString()
            )
        );
        this.session = new AuthSession(
            new PgSqlSource(hst, prt, ""),
            srnm,
            psswrd
        );
        this.host = hst;
        this.port = prt;
        this.username = srnm;
        this.password = psswrd;
        this.script = scrpt;
    }

    @Override
    public void start() throws Exception {
        new Update(
            this.session,
            new SimpleQuery(
                new FormattedText(
                    new JoinedText(
                        " ",
                        "CREATE DATABASE \"%s\" WITH OWNER \"%s\"",
                        "ENCODING 'UTF8' TEMPLATE template0;",
                        "CREATE EXTENSION IF NOT EXISTS \"pgcrypto\";"
                    ),
                    this.dbname.value(),
                    this.username
                )
            )
        ).result();
//        new Update(
//            this.session,
//            new SimpleQuery()
//        ).result();
        this.script.exec(this.session());
    }

    @Override
    public void stop() throws Exception {
        new Update(
            this.session,
            new SimpleQuery(
                new FormattedText(
                    "DROP DATABASE IF EXISTS %s",
                    this.dbname.value()
                )
            )
        ).result();
    }

    @Override
    public Session session() {
        return new AuthSession(
            new PgSqlSource(
                this.host,
                this.port,
                this.dbname.value()
            ),
            this.username,
            this.password
        );
    }
}
