/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.paginated;

import com.github.fabriciofx.cactoos.jdbc.text.Pretty;
import org.apache.calcite.sql.SqlOrderBy;
import org.apache.calcite.sql.parser.SqlParser;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

/**
 * {@link FlatOrder} tests.
 *
 * @since 0.9.0
 */
final class FlatOrderTest {

    @Test
    void keepsOrderOfPaginatedSelect() throws Exception {
        new Assertion<>(
            "must keep the ORDER BY of the select in its page",
            new Pretty(
                new FlatOrder(
                    (SqlOrderBy) SqlParser.create(
                        "SELECT name FROM satellite ORDER BY launch DESC"
                    ).parseQuery(),
                    6,
                    9
                ),
                new LimitDialect()
            ),
            new IsText(
                """
                SELECT NAME, COUNT(*) OVER () AS __total_count__ \
                FROM SATELLITE ORDER BY LAUNCH DESC LIMIT 9 OFFSET 45\
                """
            )
        ).affirm();
    }
}
