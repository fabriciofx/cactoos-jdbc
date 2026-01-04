/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.source;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.fake.logger.FakeLogger;
import com.github.fabriciofx.fake.server.db.source.H2Source;
import org.cactoos.text.Joined;
import org.cactoos.text.TextOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

/**
 * Logged Source tests.
 * @since 0.9.0
 */
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
            "must log sessions",
            new TextOf(logger.toString()),
            new IsText(
                new Joined(
                    "\n",
                    "[test] Source retrieve url: 'jdbc:h2:mem:testdb'.",
                    "[test] Source retrieve password: '********'.",
                    "[test] Session[#0] opened.",
                    "[test] Session[#0] autocommit disabled.",
                    "[test] Session[#1] opened.",
                    "[test] Session[#1] autocommit enabled.",
                    "[test] Session[#0] closing.",
                    "[test] Session[#1] closing.\n"
                )
            )
        ).affirm();
    }
}
