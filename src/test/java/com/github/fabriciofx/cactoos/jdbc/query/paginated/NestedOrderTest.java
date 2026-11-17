/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.paginated;

import com.github.fabriciofx.cactoos.jdbc.text.Pretty;
import org.apache.calcite.sql.SqlOrderBy;
import org.apache.calcite.sql.parser.SqlParser;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

/**
 * {@link NestedOrder} tests.
 *
 * @since 0.9.0
 */
final class NestedOrderTest {

    @Test
    void ordersGroupedSubquery() throws Exception {
        new Assertion<>(
            "must order the grouped subquery and count the total over it",
            new Pretty(
                new NestedOrder(
                    (SqlOrderBy) SqlParser.create(
                        """
                        SELECT orbit, COUNT(*) AS rocks FROM asteroid \
                        GROUP BY orbit ORDER BY rocks DESC\
                        """
                    ).parseQuery(),
                    3,
                    9
                ),
                new LimitDialect()
            ),
            new IsText(
                """
                SELECT *, COUNT(*) OVER () AS __total_count__ FROM \
                (SELECT ORBIT, COUNT(*) AS ROCKS FROM ASTEROID GROUP BY \
                ORBIT) AS __paginated_subquery__ ORDER BY ROCKS DESC \
                LIMIT 9 OFFSET 18\
                """
            )
        ).affirm();
    }

    @Test
    void ordersDistinctSubquery() throws Exception {
        new Assertion<>(
            "must order the distinct subquery and count the total over it",
            new Pretty(
                new NestedOrder(
                    (SqlOrderBy) SqlParser.create(
                        "SELECT DISTINCT tail FROM comet ORDER BY tail"
                    ).parseQuery(),
                    7,
                    2
                ),
                new LimitDialect()
            ),
            new IsText(
                """
                SELECT *, COUNT(*) OVER () AS __total_count__ FROM \
                (SELECT DISTINCT TAIL FROM COMET) AS \
                __paginated_subquery__ ORDER BY TAIL LIMIT 2 OFFSET 12\
                """
            )
        ).affirm();
    }
}
