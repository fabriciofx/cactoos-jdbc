/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

/**
 * QueryOf tests.
 * @since 0.9.0
 */
final class QueryOfTest {
    @Test
    void withoutValues() {
        final String sql = "SELECT * FROM employee";
        new Assertion<>(
            "must build a query without values",
            () -> new QueryOf(sql).sql(),
            new IsText(sql)
        ).affirm();
    }
}
