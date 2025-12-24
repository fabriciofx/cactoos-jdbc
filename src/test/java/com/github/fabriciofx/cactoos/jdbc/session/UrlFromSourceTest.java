/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.fake.server.db.source.H2Source;
import org.cactoos.text.FormattedText;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

/**
 * UrlFromSource tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.9.0
 * @checkstyle JavadocMethodCheck (500 lines)
 */
final class UrlFromSourceTest {
    @Test
    void url() {
        final String dbname = "testdb";
        new Assertion<>(
            "must get a JDBC url from session",
            new UrlFromSource(new H2Source(dbname)),
            new IsText(new FormattedText("jdbc:h2:mem:%s", dbname))
        ).affirm();
    }
}
