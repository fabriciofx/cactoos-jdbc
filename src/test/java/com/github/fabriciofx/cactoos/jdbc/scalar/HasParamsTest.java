/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.scalar;

import org.apache.calcite.sql.parser.SqlParser;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * {@link HasParams} tests.
 *
 * @since 0.9.0
 */
final class HasParamsTest {

    @Test
    void detectsNestedParam() throws Exception {
        new Assertion<>(
            "must detect a param nested in an expression",
            new HasParams(
                SqlParser.create("(depth + ?) * 2 > 40").parseExpression()
            ),
            new HasValue<>(true)
        ).affirm();
    }

    @Test
    void doesNotDetectParamInLiterals() throws Exception {
        new Assertion<>(
            "must not detect a param in an expression without one",
            new HasParams(
                SqlParser.create("reef = '?' AND tide < 7").parseExpression()
            ),
            new HasValue<>(false)
        ).affirm();
    }

    @Test
    void detectsParamInList() throws Exception {
        new Assertion<>(
            "must detect a param inside an IN list",
            new HasParams(
                SqlParser.create("orbit IN (3, ?, 12)").parseExpression()
            ),
            new HasValue<>(true)
        ).affirm();
    }
}
