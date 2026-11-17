/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.scalar;

import org.apache.calcite.avatica.util.Quoting;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * {@link QuotingOf} tests.
 *
 * @since 0.9.0
 */
final class QuotingOfTest {

    @Test
    void readsBackTicks() {
        new Assertion<>(
            "must read back ticks from a query that uses them",
            new QuotingOf("SELECT `rank` FROM `group` WHERE lvl = 'x'"),
            new HasValue<>(Quoting.BACK_TICK)
        ).affirm();
    }

    @Test
    void readsDoubleQuotes() {
        new Assertion<>(
            "must read double quotes from a query that uses them",
            new QuotingOf("SELECT \"Rank\" FROM \"Guild\" WHERE lvl = 'x'"),
            new HasValue<>(Quoting.DOUBLE_QUOTE)
        ).affirm();
    }

    @Test
    void readsDoubleQuotesFromUnquotedQuery() {
        new Assertion<>(
            "must read double quotes, the SQL standard, without quotes",
            new QuotingOf("SELECT rank FROM guild WHERE lvl = 'x'"),
            new HasValue<>(Quoting.DOUBLE_QUOTE)
        ).affirm();
    }
}
