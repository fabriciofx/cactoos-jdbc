/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import com.github.fabriciofx.cactoos.jdbc.query.NamedQuery;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.scalar.ResultSetAsXml;
import com.github.fabriciofx.cactoos.jdbc.source.NoAuth;
import com.github.fabriciofx.fake.server.RandomName;
import com.github.fabriciofx.fake.server.db.source.H2Source;
import com.jcabi.matchers.XhtmlMatchers;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;

/**
 * Batch tests.
 *
 * @since 0.9.0
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
final class BatchTest {
    @Test
    void batch() throws Exception {
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
                    CREATE TABLE client (id INT, name VARCHAR(50), age INT,
                    PRIMARY KEY (id))
                    """
                )
            ).execute();
            new Batch(
                session,
                new NamedQuery(
                    """
                    INSERT INTO client (id, name, age) VALUES (:id, :name,
                    :age)
                    """,
                    new ParamsOf(
                        new IntParam("id", 1),
                        new TextParam("name", "Jeff Bridges"),
                        // @checkstyle MagicNumber (1 line)
                        new IntParam("age", 34)
                    ),
                    new ParamsOf(
                        new IntParam("id", 2),
                        new TextParam("name", "Anna Miller"),
                        // @checkstyle MagicNumber (1 line)
                        new IntParam("age", 26)
                    ),
                    new ParamsOf(
                        // @checkstyle MagicNumber (3 lines)
                        new IntParam("id", 3),
                        new TextParam("name", "Michal Douglas"),
                        new IntParam("age", 32)
                    )
                )
            ).execute();
            new Assertion<>(
                "must batch clients records",
                XhtmlMatchers.xhtml(
                    new ResultSetAsXml(
                        new Select(
                            session,
                            new QueryOf("SELECT * FROM client")
                        ),
                        "clients",
                        "client"
                    ).value()
                ),
                XhtmlMatchers.hasXPaths(
                    "/clients/client/id[text()='1']",
                    "/clients/client/name[text()='Jeff Bridges']",
                    "/clients/client/age[text()='34']",
                    "/clients/client/id[text()='2']",
                    "/clients/client/name[text()='Anna Miller']",
                    "/clients/client/age[text()='26']",
                    "/clients/client/id[text()='3']",
                    "/clients/client/name[text()='Michal Douglas']",
                    "/clients/client/age[text()='32']"
                )
            ).affirm();
        }
    }
}
