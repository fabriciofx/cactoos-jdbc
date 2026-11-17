/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.normalized;

import java.util.List;
import org.apache.calcite.sql.parser.SqlParser;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * {@link Reorderable} tests.
 *
 * @since 0.9.0
 */
final class ReorderableTest {

    @Test
    void allowsReorderingWithSingleParam() throws Exception {
        new Assertion<>(
            "must allow reordering when only one node holds a param",
            new Reorderable(
                List.of(
                    SqlParser.create("lava > ?").parseExpression(),
                    SqlParser.create("ash = 6").parseExpression(),
                    SqlParser.create("vent < 2").parseExpression()
                )
            ),
            new HasValue<>(true)
        ).affirm();
    }

    @Test
    void forbidsReorderingWithManyParams() throws Exception {
        new Assertion<>(
            "must forbid reordering when two nodes hold params",
            new Reorderable(
                List.of(
                    SqlParser.create("lava > ?").parseExpression(),
                    SqlParser.create("ash = 6").parseExpression(),
                    SqlParser.create("vent < ?").parseExpression()
                )
            ),
            new HasValue<>(false)
        ).affirm();
    }
}
