/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.source;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.param.BoolParam;
import com.github.fabriciofx.cactoos.jdbc.param.DateParam;
import com.github.fabriciofx.cactoos.jdbc.param.DecimalParam;
import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.query.NamedQuery;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.statement.Insert;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import com.github.fabriciofx.fake.logger.FakeLogger;
import com.github.fabriciofx.fake.server.db.source.H2Source;
import java.sql.ResultSet;
import java.time.LocalDate;
import org.cactoos.text.Replaced;
import org.cactoos.text.TextOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasString;
import org.llorllale.cactoos.matchers.Matches;
import org.llorllale.cactoos.matchers.MatchesRegex;

/**
 * Logged Source tests.
 *
 * @since 0.9.0
 */
@SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
final class LoggedTest {
    @Test
    void logSourceAndSessionsStatements() throws Exception {
        final FakeLogger logger = new FakeLogger();
        final Source source = new Logged(
            new NoAuth(new H2Source("testdb")),
            "test",
            logger
        );
        source.url();
        source.password();
        final Session sessiona = source.session();
        sessiona.autocommit(false);
        final Session sessionb = source.session();
        sessionb.autocommit(true);
        sessiona.close();
        sessionb.close();
        new Assertion<>(
            "must log source and sessions",
            new HasString(
                """
                [test] Source retrieve url: 'jdbc:h2:mem:testdb'
                [test] Source retrieve password: '********'
                [test] Session[#0] opened
                [test] Session[#0] autocommit disabled
                [test] Session[#1] opened
                [test] Session[#1] autocommit enabled
                [test] Session[#0] closed
                [test] Session[#1] closed
                """
            ),
            new Matches<>(
                new Replaced(
                    new TextOf(logger.toString()),
                    "\r\n",
                    "\n"
                )
            )
        ).affirm();
    }

    @Test
    void logAllSelectSteps() throws Exception {
        final FakeLogger logger = new FakeLogger();
        final Source source = new Logged(
            new NoAuth(new H2Source("testdb")),
            "test",
            logger
        );
        try (Session session = source.session()) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE person (id INT, name VARCHAR(30), \
                    created_at DATE, city VARCHAR(20), working \
                    BOOLEAN, height DECIMAL(20,2), PRIMARY KEY (id))\
                    """
                )
            ).execute();
            new Insert(
                session,
                new NamedQuery(
                    """
                    INSERT INTO person (id, name, created_at, city, \
                    working, height) VALUES (:id, :name, :created_at, \
                    :city, :working, :height)\
                    """,
                    new IntParam("id", 1),
                    new TextParam("name", "Rob Pike"),
                    new DateParam("created_at", LocalDate.now()),
                    new TextParam("city", "San Francisco"),
                    new BoolParam("working", true),
                    new DecimalParam("height", "1.86")
                )
            ).execute();
            try (
                ResultSet rset = new Select(
                    session,
                    new NamedQuery(
                        "SELECT id, name FROM person WHERE id = :id",
                        new IntParam("id", 1)
                    )
                ).execute()
            ) {
                if (rset.next()) {
                    rset.getString("name");
                }
            }
        }
        new Assertion<>(
            "must log if session has been opened and closed",
            new MatchesRegex(
                """
                (?s)\\[test\\] Session\\[#0\\] opened.*\
                \\[test\\] Session\\[#0\\] closed\\s+\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must log if PreparedStatement #0 is created and closed",
            new MatchesRegex(
                """
                (?s).*\\[test\\] Connection\\[#0\\] created \
                PreparedStatement\\[#0\\].*\\[test\\] \
                PreparedStatement\\[#0\\] closed.*\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must log if PreparedStatement #1 is created and closed",
            new MatchesRegex(
                """
                (?s).*\\[test\\] Connection\\[#0\\] created \
                PreparedStatement\\[#1\\].*\\[test\\] \
                PreparedStatement\\[#1\\] closed.*\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must log if PreparedStatement #2 is created and closed",
            new MatchesRegex(
                """
                (?s).*\\[test\\] Connection\\[#0\\] created \
                PreparedStatement\\[#2\\].*\\[test\\] \
                PreparedStatement\\[#2\\] closed.*\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
    }
}
