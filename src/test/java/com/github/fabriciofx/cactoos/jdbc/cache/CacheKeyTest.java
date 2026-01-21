/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.jdbc.param.BoolParam;
import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.query.NamedQuery;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsTrue;

/**
 * CacheKey tests.
 * @since 0.9.0
 */
@SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
final class CacheKeyTest {
    @Test
    void producesSameHashesForSameContent() {
        new Assertion<>(
            "must produces the same hash for the same content",
            new CacheKey(
                new NamedQuery(
                    "SELECT id, name FROM person WHERE id = :id",
                    new IntParam("id", 1)
                )
            ).equals(
                new CacheKey(
                    new NamedQuery(
                        "SELECT id, name FROM person WHERE id = :id",
                        new IntParam("id", 1)
                    )
                )
            ),
            new IsTrue()
        ).affirm();
    }

    @Test
    void producesDifferentHashesForDifferentContentTypes() {
        new Assertion<>(
            "must produces the different hashes for different content types",
            new CacheKey(
                new NamedQuery(
                    "SELECT * FROM person WHERE active = :active",
                    new IntParam("active", 1)
                )
            ).equals(
                new CacheKey(
                    new NamedQuery(
                        "SELECT * FROM person WHERE active = :active",
                        new BoolParam("active", true)
                    )
                )
            ),
            new IsNot<>(new IsTrue())
        ).affirm();
    }
}
