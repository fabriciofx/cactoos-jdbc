/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.query.NamedQuery;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.source.NoAuth;
import com.github.fabriciofx.fake.server.RandomName;
import com.github.fabriciofx.fake.server.db.source.H2Source;
import org.cactoos.scalar.ScalarOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * Insert tests.
 *
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class InsertTest {
    @Test
    void insert() throws Exception {
        try (
            Session session = new NoAuth(
                new H2Source(
                    new RandomName().asString()
                )
            ).session()
        ) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE t01 (id INT, name VARCHAR(50),
                    PRIMARY KEY (id))
                    """
                )
            ).execute();
            new Assertion<>(
                "must insert into table",
                new ScalarOf<>(
                    () -> new Insert(
                        session,
                        new NamedQuery(
                            "INSERT INTO t01 (id, name) VALUES (:id, :name)",
                            new IntParam("id", 1),
                            new TextParam("name", "Yegor Bugayenko")
                        )
                    ).execute()
                ),
                new HasValue<>(false)
            ).affirm();
        }
    }
}
