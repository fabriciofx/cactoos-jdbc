/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018-2025 Fabrício Barros Cabral
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
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Server;
import com.github.fabriciofx.cactoos.jdbc.Servers;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.param.IntOf;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.query.WithKey;
import com.github.fabriciofx.cactoos.jdbc.result.ResultAsValue;
import com.github.fabriciofx.cactoos.jdbc.server.H2Server;
import com.github.fabriciofx.cactoos.jdbc.server.MysqlServer;
import com.github.fabriciofx.cactoos.jdbc.server.PgsqlServer;
import java.math.BigInteger;
import org.cactoos.text.Joined;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * StatementInsert tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings(
    {
        "PMD.AvoidDuplicateLiterals",
        "PMD.AvoidInstantiatingObjectsInLoops",
        "PMD.TestClassWithoutTestCases"
    }
)
final class InsertTest {
    @Test
    void insert() throws Exception {
        try (
            Servers servers = new Servers(
                new H2Server(),
                new MysqlServer(),
                new PgsqlServer()
            )
        ) {
            for (final Session session : servers.sessions()) {
                new Update(
                    session,
                    new QueryOf(
                        new Joined(
                            " ",
                            "CREATE TABLE t01 (id INT, name VARCHAR(50),",
                            "PRIMARY KEY (id))"
                        )
                    )
                ).result();
                MatcherAssert.assertThat(
                    "Can't insert into table",
                    new ResultAsValue<>(
                        new Insert(
                            session,
                            new QueryOf(
                                new Joined(
                                    " ",
                                    "INSERT INTO t01 (id, name)",
                                    "VALUES (:id, :name)"
                                ),
                                new IntOf("id", 1),
                                new TextOf("name", "Yegor Bugayenko")
                            )
                        )
                    ),
                    new HasValue<>(false)
                );
            }
        }
    }

    @Test
    // @checkstyle MethodNameCheck (1 line)
    void insertWithKeysH2() throws Exception {
        try (Server server = new H2Server()) {
            server.start();
            final Session session = server.session();
            new Update(
                session,
                new QueryOf(
                    new Joined(
                        " ",
                        "CREATE TABLE t02 (id INT AUTO_INCREMENT,",
                        "name VARCHAR(50), PRIMARY KEY (id))"
                    )
                )
            ).result();
            MatcherAssert.assertThat(
                "Can't insert with an integer keys",
                new ResultAsValue<>(
                    new InsertWithKey<>(
                        session,
                        new WithKey(
                            "INSERT INTO t02 (name) VALUES (:name)",
                            new TextOf("name", "Jeff Malony")
                        )
                    )
                ),
                new HasValue<>(1)
            );
        }
    }

    @Test
    void insertWithKeysPsql() throws Exception {
        try (Server server = new PgsqlServer()) {
            server.start();
            final Session session = server.session();
            new Update(
                session,
                new QueryOf(
                    new Joined(
                        " ",
                        "CREATE TABLE t02 (id SERIAL,",
                        "name VARCHAR(50), PRIMARY KEY (id))"
                    )
                )
            ).result();
            MatcherAssert.assertThat(
                "Can't insert with an integer keys",
                new ResultAsValue<>(
                    new InsertWithKey<>(
                        session,
                        new WithKey(
                            "INSERT INTO t02 (name) VALUES (:name)",
                            new TextOf("name", "Jeff Malony")
                        )
                    )
                ),
                new HasValue<>(1)
            );
        }
    }

    @Test
    void insertWithKeysMysql() throws Exception {
        try (Server server = new MysqlServer()) {
            server.start();
            final Session session = server.session();
            new Update(
                session,
                new QueryOf(
                    new Joined(
                        " ",
                        "CREATE TABLE t02 (id INT AUTO_INCREMENT,",
                        "name VARCHAR(50), PRIMARY KEY (id))"
                    )
                )
            ).result();
            MatcherAssert.assertThat(
                "Can't insert with an integer keys",
                new ResultAsValue<>(
                    new InsertWithKey<>(
                        session,
                        new WithKey(
                            () -> "INSERT INTO t02 (name) VALUES (:name)",
                            "id",
                            new TextOf("name", "Jeff Malony")
                        )
                    )
                ),
                new HasValue<>(BigInteger.ONE)
            );
        }
    }
}
