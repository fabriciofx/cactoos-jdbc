/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.param.IntOf;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.query.WithKey;
import com.github.fabriciofx.cactoos.jdbc.result.ResultAsValue;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuth;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.Servers;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import com.github.fabriciofx.fake.server.db.server.MysqlServer;
import com.github.fabriciofx.fake.server.db.server.PgsqlServer;
import java.math.BigInteger;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * Insert tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @since 0.1
 */
final class InsertTest {
    @Test
    void insert() throws Exception {
        try (
            Servers<DataSource> servers = new Servers<>(
                new H2Server(),
                new MysqlServer(),
                new PgsqlServer()
            )
        ) {
            for (final DataSource source : servers.resources()) {
                final Session session = new NoAuth(source);
                new Update(
                    session,
                    new QueryOf(
                        "CREATE TABLE t01 (id INT, name VARCHAR(50), PRIMARY KEY (id))"
                    )
                ).result();
                new Assertion<>(
                    "must insert into table",
                    new ResultAsValue<>(
                        new Insert(
                            session,
                            new QueryOf(
                                "INSERT INTO t01 (id, name) VALUES (:id, :name)",
                                new IntOf("id", 1),
                                new TextOf("name", "Yegor Bugayenko")
                            )
                        )
                    ),
                    new HasValue<>(false)
                ).affirm();
            }
        }
    }

    @Test
        // @checkstyle MethodNameCheck (1 line)
    void insertWithKeysH2() throws Exception {
        try (Server<DataSource> server = new H2Server()) {
            server.start();
            final Session session = new NoAuth(server.resource());
            new Update(
                session,
                new QueryOf(
                    "CREATE TABLE t02 (id INT AUTO_INCREMENT, name VARCHAR(50), PRIMARY KEY (id))"
                )
            ).result();
            new Assertion<>(
                "must insert with an integer keys in H2",
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
            ).affirm();
        }
    }

    @Test
    void insertWithKeysPsql() throws Exception {
        try (Server<DataSource> server = new PgsqlServer()) {
            server.start();
            final Session session = new NoAuth(server.resource());
            new Update(
                session,
                new QueryOf(
                    "CREATE TABLE t02 (id SERIAL, name VARCHAR(50), PRIMARY KEY (id))"
                )
            ).result();
            new Assertion<>(
                "must insert with an integer keys in PostgreSQL",
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
            ).affirm();
        }
    }

    @Test
    void insertWithKeysMysql() throws Exception {
        try (Server<DataSource> server = new MysqlServer()) {
            server.start();
            final Session session = new NoAuth(server.resource());
            new Update(
                session,
                new QueryOf(
                    "CREATE TABLE t02 (id INT AUTO_INCREMENT, name VARCHAR(50), PRIMARY KEY (id))"
                )
            ).result();
            new Assertion<>(
                "must insert with an integer keys in MySQL",
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
            ).affirm();
        }
    }
}
