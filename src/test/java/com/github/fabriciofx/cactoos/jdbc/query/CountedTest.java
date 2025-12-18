/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.IsText;

/**
 * Counted query tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.8.0
 * @checkstyle JavadocMethodCheck (500 lines)
 */
final class CountedTest {
    @Test
    void withCountForAllColumns() throws Exception {
        MatcherAssert.assertThat(
            "This query contains a count statement",
            new Counted(new QueryOf("SELECT COUNT(*) FROM employee")),
            new IsText("SELECT COUNT(*) FROM employee")
        );
    }

    @Test
    void withCountForOneColumn() throws Exception {
        MatcherAssert.assertThat(
            "This query contains a count statement",
            new Counted(new QueryOf("SELECT COUNT(id) FROM employee")),
            new IsText("SELECT COUNT(id) FROM employee")
        );
    }

    @Test
    void withoutCount() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
                new Counted(new QueryOf("SELECT * FROM employee")).asString();
            }
        );
    }
}
