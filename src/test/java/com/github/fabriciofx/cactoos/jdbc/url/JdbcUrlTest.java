/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.url;

import com.github.fabriciofx.cactoos.jdbc.text.JdbcUrl;
import com.github.fabriciofx.fake.server.db.source.H2Source;
import org.cactoos.text.FormattedText;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

/**
 * JdbcUrl tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.9.0
 * @checkstyle JavadocMethodCheck (500 lines)
 */
@SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
final class JdbcUrlTest {
    @Test
    void url() {
        final String dbname = "testdb";
        new Assertion<>(
            "must get a JDBC url from source",
            new JdbcUrl(new H2Source(dbname)),
            new IsText(new FormattedText("jdbc:h2:mem:%s", dbname))
        ).affirm();
    }
}
