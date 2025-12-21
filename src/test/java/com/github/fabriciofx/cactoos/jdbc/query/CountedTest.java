/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import org.cactoos.scalar.ScalarOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;
import org.llorllale.cactoos.matchers.Matches;
import org.llorllale.cactoos.matchers.Throws;

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
    void withCountForAllColumns() {
        final String sql = "SELECT COUNT(*) FROM employee";
        new Assertion<>(
            "must contains a count query for all columns",
            new Counted(new QueryOf(sql)),
            new IsText(sql)
        ).affirm();
    }

    @Test
    void withCountForOneColumn() {
        final String sql = "SELECT COUNT(id) FROM employee";
        new Assertion<>(
            "must contains a count query for one column",
            new Counted(new QueryOf(sql)),
            new IsText(sql)
        );
    }

    @Test
    void withoutCount() {
        new Assertion<>(
            "must throws an exception if is not a count query",
            new Throws<>(IllegalArgumentException.class),
            new Matches<>(
                new ScalarOf<>(
                    () -> new Counted(new QueryOf("SELECT * FROM employee"))
                        .asString()
                )
            )
        ).affirm();
    }
}
