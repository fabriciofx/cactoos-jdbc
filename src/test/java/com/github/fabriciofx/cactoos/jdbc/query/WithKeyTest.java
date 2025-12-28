/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

/**
 * WithKey query tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.9.0
 * @checkstyle JavadocMethodCheck (500 lines)
 */
final class WithKeyTest {
    @Test
    void withKey() {
        new Assertion<>(
            "must build a with key query",
            new KeyedQuery(
                () -> "INSERT INTO contact (name) VALUES (:name)",
                new TextOf("name", "Leonardo da Vinci")
            ),
            new IsText("INSERT INTO contact (name) VALUES (?)")
        ).affirm();
    }
}
