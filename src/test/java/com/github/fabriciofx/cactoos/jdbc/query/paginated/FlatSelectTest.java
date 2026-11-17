/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.paginated;

import com.github.fabriciofx.cactoos.jdbc.text.Pretty;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.parser.SqlParser;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

/**
 * {@link FlatSelect} tests.
 *
 * @since 0.9.0
 */
final class FlatSelectTest {

    @Test
    void countsTotalInSelectItself() throws Exception {
        new Assertion<>(
            "must count the total in the select itself, with no subquery",
            new Pretty(
                new FlatSelect(
                    (SqlSelect) SqlParser.create(
                        "SELECT name, mass FROM planet WHERE mass > 3"
                    ).parseQuery(),
                    4,
                    6
                ),
                new LimitDialect()
            ),
            new IsText(
                """
                SELECT NAME, MASS, COUNT(*) OVER () AS __total_count__ \
                FROM PLANET WHERE MASS > 3 LIMIT 6 OFFSET 18\
                """
            )
        ).affirm();
    }
}
