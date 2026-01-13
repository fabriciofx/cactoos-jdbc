/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Session;
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
 * KeyedInsert tests.
 *
 * @since 0.9.0
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class KeyedInsertTest {
    @Test
    void insertWithKeys() throws Exception {
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
                    CREATE TABLE contact (id INT AUTO_INCREMENT, name
                    VARCHAR(50) NOT NULL, CONSTRAINT pk_contact PRIMARY KEY(id))
                    """
                )
            ).execute();
            new Assertion<>(
                "must generated key value",
                new ScalarOf<>(
                    () -> new KeyedInsert<>(
                        session,
                        new NamedQuery(
                            "INSERT INTO contact (name) VALUES (:name)",
                            new TextParam("name", "Leonardo da Vinci")
                        )
                    ).execute()
                ),
                new HasValue<>(1)
            ).affirm();
        }
    }
}
