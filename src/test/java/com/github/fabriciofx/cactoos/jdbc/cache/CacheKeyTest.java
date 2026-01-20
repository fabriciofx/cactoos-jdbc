/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.cache.Key;
import com.github.fabriciofx.cactoos.jdbc.Query;
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
final class CacheKeyTest {
    @Test
    void producesSameHashesForSameContent() {
        final Key<Query> keya = new CacheKey(
            new NamedQuery(
                "SELECT id, name FROM person WHERE id = :id",
                new IntParam("id", 1)
            )
        );
        final Key<Query> keyb = new CacheKey(
            new NamedQuery(
                "SELECT id, name FROM person WHERE id = :id",
                new IntParam("id", 1)
            )
        );
        new Assertion<>(
            "must produces the same hash for the same content",
            keya.equals(keyb),
            new IsTrue()
        ).affirm();
    }

    @Test
    void producesDifferentHashesForDifferentContentTypes() {
        final Key<Query> keya = new CacheKey(
            new NamedQuery(
                "SELECT * FROM person WHERE active = :active",
                new IntParam("active", 1)
            )
        );
        final Key<Query> keyb = new CacheKey(
            new NamedQuery(
                "SELECT * FROM person WHERE active = :active",
                new BoolParam("active", true)
            )
        );
        new Assertion<>(
            "must produces the same hash for the same content",
            keya.equals(keyb),
            new IsNot<>(new IsTrue())
        ).affirm();
    }
}
