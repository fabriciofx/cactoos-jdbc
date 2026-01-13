/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import org.cactoos.text.Concatenated;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

/**
 * Paginated tests.
 *
 * @since 0.8.0
 */
final class PaginatedTest {
    @Test
    void paginated() {
        new Assertion<>(
            "must contains a count query for all columns",
            () -> new Paginated(
                new QueryOf("SELECT name, address FROM employee"),
                20,
                0
            ).sql(),
            new IsText(
                new Concatenated(
                    "SELECT q.*, COUNT(*) OVER () AS __total__ FROM ",
                    "(SELECT name, address FROM employee) q LIMIT ? OFFSET ?"
                )
            )
        ).affirm();
    }
}
