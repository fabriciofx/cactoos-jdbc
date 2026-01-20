/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasString;
import org.llorllale.cactoos.matchers.Matches;

/**
 * Pretty tests.
 * @since 0.9.0
 */
final class PrettyTest {
    @Test
    void pretty() throws Exception {
        new Assertion<>(
            "must eliminate new lines and duplicated white space",
            new HasString(
                """
                CREATE TABLE person (id INT, name VARCHAR(30), \
                created_at DATE, city VARCHAR(20), working BOOLEAN, \
                height DECIMAL(20,2), PRIMARY KEY (id))\
                """
            ),
            new Matches<>(
                () -> new Pretty(
                    new QueryOf(
                        """
                        CREATE TABLE person (id INT, name VARCHAR(30),
                        created_at DATE, city VARCHAR(20), working
                        BOOLEAN, height DECIMAL(20,2), PRIMARY KEY (id))
                        """
                    )
                ).sql()
            )
        ).affirm();
    }
}
