/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.text;

import org.apache.calcite.sql.parser.SqlParser;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

/**
 * {@link Pretty} tests.
 *
 * @since 0.9.0
 */
final class PrettyTest {

    @Test
    void doesNotQuoteRegularIdentifiers() throws Exception {
        new Assertion<>(
            "must not quote identifiers that do not need quotes",
            new Pretty(
                SqlParser.create("SELECT KELVIN FROM STAR WHERE MASS > 2")
                    .parseQuery()
            ),
            new IsText("SELECT KELVIN FROM STAR WHERE MASS > 2")
        ).affirm();
    }
}
