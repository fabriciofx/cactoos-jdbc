/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.source.Logged;
import com.github.fabriciofx.cactoos.jdbc.source.NoAuth;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import com.github.fabriciofx.fake.logger.FakeLogger;
import com.github.fabriciofx.fake.server.db.source.H2Source;
import java.util.logging.Logger;
import org.cactoos.text.TextOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasString;
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
    @Test
    void logAnUpdate() throws Exception {
        final Logger logger = new FakeLogger();
        try (
            Session session = new Logged(
                new NoAuth(new H2Source("testdb")),
                "test",
                logger
            ).session()
        ) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE t012 (id INT AUTO_INCREMENT, name VARCHAR(50))
                    """
                )
            ).execute();
        }
        new Assertion<>(
            "must check if session opens and closes with an update",
            new MatchesRegex(
                """
                (?s)\\[test\\] Session\\[#0\\] opened.*\
                \\[test\\] Session\\[#0\\] closed\\s+\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must check if a connection created a prepared statement",
            new HasString(
                "[test] Connection[#0] created PreparedStatement[#0] using SQL"
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must check if the prepared statement executed an update",
            new HasString(
                "[test] PreparedStatement[#0] executed an update and returned '0'"
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must check if the prepared is closed",
            new HasString("[test] PreparedStatement[#0] closed in"),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
    }
}
