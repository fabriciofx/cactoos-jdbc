/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.connection;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.session.Logged;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuth;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import com.github.fabriciofx.fake.logger.FakeLogger;
import com.github.fabriciofx.fake.server.db.source.H2Source;
import java.util.logging.Logger;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * StatementInsert tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
final class LoggedTest {
    @Test
    void loggedUpdate() throws Exception {
        final Logger logger = new FakeLogger();
        final String sql =
            "CREATE TABLE t012 (id INT AUTO_INCREMENT, name VARCHAR(50))";
        final Session session = new Logged(
            new NoAuth(
                new H2Source("testdb")
            ),
            "cactoos-jdbc",
            logger
        );
        new Update(session, new QueryOf(sql)).result();
        MatcherAssert.assertThat(
            "Can't connection from cactoos-jdbc",
            logger.toString(),
            Matchers.allOf(
                Matchers.containsString(
                    "Connection[#0] has been opened with properties numServers=0"
                ),
                Matchers.containsString(
                    String.format(
                        "PreparedStatement[#0] created using SQL '%s'",
                        sql
                    )
                ),
                Matchers.containsString(
                    "PreparedStatement[#0] updated a source and returned '0' in"
                ),
                Matchers.containsString("PreparedStatement[#0] closed")
            )
        );
    }
}
