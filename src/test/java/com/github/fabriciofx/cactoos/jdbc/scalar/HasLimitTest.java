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
 * {@link HasLimit} tests.
 *
 * @since 0.9.0
 */
final class HasLimitTest {

    @Test
    void detectsLimit() throws Exception {
        new Assertion<>(
            "must detect a select with LIMIT",
            new HasLimit(
                SqlParser.create("SELECT x FROM nebula LIMIT 3").parseQuery()
            ),
            new HasValue<>(true)
        ).affirm();
    }

    @Test
    void detectsOffset() throws Exception {
        new Assertion<>(
            "must detect a select with only OFFSET",
            new HasLimit(
                SqlParser.create("SELECT x FROM quasar OFFSET 12").parseQuery()
            ),
            new HasValue<>(true)
        ).affirm();
    }

    @Test
    void detectsFetch() throws Exception {
        new Assertion<>(
            "must detect a select with FETCH",
            new HasLimit(
                SqlParser.create(
                    "SELECT x FROM pulsar FETCH NEXT 5 ROWS ONLY"
                ).parseQuery()
            ),
            new HasValue<>(true)
        ).affirm();
    }

    @Test
    void doesNotDetectOrderedSelectWithoutLimit() throws Exception {
        new Assertion<>(
            "must not detect LIMIT in an ordered select without it",
            new HasLimit(
                SqlParser.create("SELECT x FROM galaxy ORDER BY x").parseQuery()
            ),
            new HasValue<>(false)
        ).affirm();
    }

    @Test
    void doesNotDetectPlainSelect() throws Exception {
        new Assertion<>(
            "must not detect LIMIT in a select without it",
            new HasLimit(
                SqlParser.create("SELECT x FROM asteroid").parseQuery()
            ),
            new HasValue<>(false)
        ).affirm();
    }
}
