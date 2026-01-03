/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.connection;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.session.Logged;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuth;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import com.github.fabriciofx.fake.logger.FakeLogger;
import com.github.fabriciofx.fake.server.db.source.H2Source;
import java.util.logging.Logger;
import org.cactoos.Text;
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
@SuppressWarnings("PMD.UnnecessaryLocalRule")
final class LoggedTest {
    @Disabled
    @Test
    void loggedUpdate() throws Exception {
        final Logger logger = new FakeLogger();
        try (
            Connexio connexio = new Logged(
                new NoAuth(new H2Source("testdb")),
                "cactoos-jdbc",
                logger
            ).connexio()
        ) {
            new Update(
                connexio,
                new QueryOf("CREATE TABLE t012 (id INT AUTO_INCREMENT, name VARCHAR(50))")
            ).execute();
            final Text regex = new Joined(
                "\n",
                "(?m)^\\[cactoos-jdbc\\] Connection\\[#0\\] has been opened with properties numServers=0,\\s*",
                "\\[cactoos-jdbc\\] connection created PreparedStatement\\[#0\\] using SQL 'CREATE TABLE .*?' in \\d+ns\\.",
                "\\[cactoos-jdbc\\] PreparedStatement\\[#0\\] updated a source and returned '\\d+' in \\d+ns\\.",
                "\\[cactoos-jdbc\\] PreparedStatement\\[#0\\] closed in \\d+ns\\.\n$"
            );
            new Assertion<>(
                "must log a cactoos-jdbc update statement",
                new MatchesRegex(regex),
                new Matches<>(new TextOf(logger.toString()))
            ).affirm();
        }
    }
}
