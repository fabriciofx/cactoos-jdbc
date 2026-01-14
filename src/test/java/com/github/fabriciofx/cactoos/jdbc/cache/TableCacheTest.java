/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import com.github.fabriciofx.cactoos.jdbc.Columns;
import com.github.fabriciofx.cactoos.jdbc.Row;
import com.github.fabriciofx.cactoos.jdbc.Rows;
import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.columns.LinkedColumns;
import com.github.fabriciofx.cactoos.jdbc.row.LinkedRow;
import com.github.fabriciofx.cactoos.jdbc.rows.LinkedRows;
import com.github.fabriciofx.cactoos.jdbc.table.FakeTable;
import com.github.fabriciofx.fake.logger.FakeLogger;
import java.sql.Types;
import org.cactoos.text.TextOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.Matches;
import org.llorllale.cactoos.matchers.MatchesRegex;

/**
 * TableCache tests.
 * @since 0.9.0
 */
final class TableCacheTest {
    @Test
    void showStatistics() {
        final FakeLogger logger = new FakeLogger();
        final Cache<String, Table> cache = new Logged<>(
            new TableCache(),
            "cache",
            logger
        );
        final Row rowa = new LinkedRow();
        rowa.add("name", "Maria");
        rowa.add("age", 34);
        final Row rowb = new LinkedRow();
        rowa.add("name", "John");
        rowa.add("age", 25);
        final Rows rows = new LinkedRows();
        rows.add(rowa);
        rows.add(rowb);
        final Columns columns = new LinkedColumns();
        columns.add("name", Types.VARCHAR);
        columns.add("age", Types.INTEGER);
        cache.store("a", new FakeTable(rows, columns));
        cache.contains("a");
        cache.contains("b");
        cache.retrieve("a");
        cache.delete("a");
        cache.statistics();
        cache.clear();
        new Assertion<>(
            "must log cache statistics",
            new MatchesRegex(
                """
                (?s).*\\[cache\\] Cache statistics:
                \\s+hits: 1
                \\s+lookups: 3
                \\s+invalidations: 1
                \\s+misses: 1
                \\s+evictions: 0.*\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
    }
}
