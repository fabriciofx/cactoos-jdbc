/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.result;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.query.NamedQuery;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.scalar.ResultSetAsValue;
import com.github.fabriciofx.cactoos.jdbc.source.NoAuth;
import com.github.fabriciofx.cactoos.jdbc.statement.Insert;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import com.github.fabriciofx.fake.server.RandomName;
import com.github.fabriciofx.fake.server.db.source.H2Source;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * ResultSetAsValue tests.
 *
 * @since 0.9.0
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
final class ResultSetAsValueTest {
    @Test
    void value() throws Exception {
        final Source source = new NoAuth(
            new H2Source(
                new RandomName().asString()
            )
        );
        try (Session session = source.session()) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE contact (id INT AUTO_INCREMENT, name
                    VARCHAR(50) NOT NULL, CONSTRAINT pk_contact PRIMARY
                    KEY(id))
                    """
                )
            ).execute();
            new Insert(
                session,
                new NamedQuery(
                    "INSERT INTO contact (name) VALUES (:name)",
                    new TextParam("name", "Joseph Klimber")
                )
            ).execute();
            new Assertion<>(
                "must generated key value",
                new ResultSetAsValue<>(
                    new Select(
                        session,
                        new NamedQuery(
                            "SELECT name FROM contact WHERE id = :id",
                            new IntParam("id", 1)
                        )
                    )
                ),
                new HasValue<>("Joseph Klimber")
            ).affirm();
        }
    }
}
