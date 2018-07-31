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
package com.github.fabriciofx.cactoos.jdbc;

import com.github.fabriciofx.cactoos.jdbc.query.SimpleQuery;
import com.github.fabriciofx.cactoos.jdbc.session.AuthSession;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuthSession;
import com.github.fabriciofx.cactoos.jdbc.stmt.Update;
import org.cactoos.text.JoinedText;

public final class Sources implements Server {
    private final Session[] sessions;

    public Sources() {
        this(
            new NoAuthSession(
                new H2Source("testdb")
            ),
            new AuthSession(
                new MySqlSource("testdb"),
                "root",
                ""
            )
        );
    }

    public Sources(final Session... sssns) {
        this.sessions = sssns;
    }

    @Override
    public void start() throws Exception {
        new Update(
            new AuthSession(
                new MySqlSource(""),
                "root",
                ""
            ),
            new SimpleQuery(
                new JoinedText(
                    " ",
                    "CREATE DATABASE IF NOT EXISTS testdb CHARACTER",
                    "SET utf8mb4 COLLATE utf8mb4_unicode_ci"
                )
            )
        ).result();
    }

    @Override
    public void stop() throws Exception {
        new Update(
            new AuthSession(
                new MySqlSource(""),
                "root",
                ""
            ),
            new SimpleQuery("DROP DATABASE IF EXISTS testdb")
        ).result();
    }

    public Session[] sessions() {
        return this.sessions;
    }
}
