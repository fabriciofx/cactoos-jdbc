/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.cache.key.KeyOf;
import com.github.fabriciofx.cactoos.jdbc.param.BoolParam;
import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;
import org.llorllale.cactoos.matchers.IsTrue;

/**
 * QueryOf tests.
 * @since 0.9.0
 */
@SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
final class QueryOfTest {
    @Test
    void withoutValues() {
        new Assertion<>(
            "must build a query without values",
            () -> new QueryOf("SELECT * FROM employee").sql(),
            new IsText("SELECT * FROM employee")
        ).affirm();
    }

    @Test
    void equalsForSameContent() {
        new Assertion<>(
            "must produces the same hash for the same content",
            new KeyOf<>(
                new NamedQuery(
                    "SELECT id, name FROM person WHERE id = :id",
                    new IntParam("id", 1)
                )
            ).equals(
                new KeyOf<>(
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
    void differentForDifferentParamsTypes() {
        new Assertion<>(
            "must produces the different hashes for different content types",
            new KeyOf<>(
                new NamedQuery(
                    "SELECT * FROM person WHERE active = :active",
                    new IntParam("active", 1)
                )
            ).equals(
                new KeyOf<>(
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
