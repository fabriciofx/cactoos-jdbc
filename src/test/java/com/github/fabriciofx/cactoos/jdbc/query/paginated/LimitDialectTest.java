/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.paginated;

import com.github.fabriciofx.cactoos.jdbc.text.Pretty;
import org.apache.calcite.sql.parser.SqlParser;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

/**
 * {@link LimitDialect} tests.
 *
 * @since 0.9.0
 */
final class LimitDialectTest {

    @Test
    void printsFetchAsLimitAndOffset() {
        new Assertion<>(
            "must print OFFSET and FETCH as LIMIT and OFFSET",
            new Pretty(
                () -> SqlParser.create(
                    "SELECT name FROM comet OFFSET 14 ROWS FETCH NEXT 7 ROWS ONLY"
                ).parseQuery(),
                new LimitDialect()
            ),
            new IsText("SELECT NAME FROM COMET LIMIT 7 OFFSET 14")
        ).affirm();
    }
}
