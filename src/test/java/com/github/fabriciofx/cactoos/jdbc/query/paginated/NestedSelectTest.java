/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.paginated;

import com.github.fabriciofx.cactoos.jdbc.text.Pretty;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.parser.SqlParser;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

/**
 * {@link NestedSelect} tests.
 *
 * @since 0.9.0
 */
final class NestedSelectTest {

    @Test
    void countsTotalOverGroupedSubquery() throws Exception {
        new Assertion<>(
            "must count the total over the grouped select in a subquery",
            new Pretty(
                new NestedSelect(
                    (SqlSelect) SqlParser.create(
                        "SELECT star, COUNT(*) AS moons FROM moon GROUP BY star"
                    ).parseQuery(),
                    2,
                    8
                ),
                new LimitDialect()
            ),
            new IsText(
                """
                SELECT *, COUNT(*) OVER () AS `__total_count__` FROM \
                (SELECT `STAR`, COUNT(*) AS `MOONS` FROM `MOON` GROUP BY \
                `STAR`) AS `__paginated_subquery__` LIMIT 8 OFFSET 8\
                """
            )
        ).affirm();
    }

    @Test
    void countsTotalOverDistinctSubquery() throws Exception {
        new Assertion<>(
            "must count the total over the distinct select in a subquery",
            new Pretty(
                new NestedSelect(
                    (SqlSelect) SqlParser.create(
                        "SELECT DISTINCT galaxy FROM nebula"
                    ).parseQuery(),
                    5,
                    3
                ),
                new LimitDialect()
            ),
            new IsText(
                """
                SELECT *, COUNT(*) OVER () AS `__total_count__` FROM \
                (SELECT DISTINCT `GALAXY` FROM `NEBULA`) AS \
                `__paginated_subquery__` LIMIT 3 OFFSET 12\
                """
            )
        ).affirm();
    }
}
