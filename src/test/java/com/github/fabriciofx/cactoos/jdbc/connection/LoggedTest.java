/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.connection;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.source.Logged;
import com.github.fabriciofx.cactoos.jdbc.source.NoAuth;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import com.github.fabriciofx.fake.logger.FakeLogger;
import com.github.fabriciofx.fake.server.db.source.H2Source;
import java.util.logging.Logger;
import org.cactoos.text.Joined;
import org.cactoos.text.TextOf;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.Matches;
import org.llorllale.cactoos.matchers.MatchesRegex;

/**
 * Logged tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 */
@SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
final class LoggedTest {
    @Disabled
    @Test
    void loggedUpdate() throws Exception {
        final Logger logger = new FakeLogger();
        try (
            Session session = new Logged(
                new NoAuth(new H2Source("testdb")),
                "cactoos-jdbc",
                logger
            ).session()
        ) {
            new Update(
                session,
                new QueryOf("CREATE TABLE t012 (id INT AUTO_INCREMENT, name VARCHAR(50))")
            ).execute();
            new Assertion<>(
                "must log a cactoos-jdbc update statement",
                new MatchesRegex(
                    new Joined(
                        "\n",
                        "(?m)^\\[cactoos-jdbc\\] Connection\\[#0\\] has been opened with properties numServers=0,\\s*",
                        "\\[cactoos-jdbc\\] connection created PreparedStatement\\[#0\\] using SQL 'CREATE TABLE .*?' in \\d+ns\\.",
                        "\\[cactoos-jdbc\\] PreparedStatement\\[#0\\] updated a source and returned '\\d+' in \\d+ns\\.",
                        "\\[cactoos-jdbc\\] PreparedStatement\\[#0\\] closed in \\d+ns\\.\n$"
                    )
                ),
                new Matches<>(new TextOf(logger.toString()))
            ).affirm();
        }
    }
}
