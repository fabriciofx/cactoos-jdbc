/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

/**
 * {@link MysqlPaginated} tests.
 *
 * @since 0.9.0
 */
final class MysqlPaginatedTest {

    @Test
    void paginatesSelectWithLimitAndOffset() {
        new Assertion<>(
            "must paginate a select using LIMIT and OFFSET",
            () -> new MysqlPaginated(
                new QueryOf("SELECT * FROM employee"),
                4,
                40
            ).sql(),
            new IsText(
                """
                SELECT *, COUNT(*) OVER () AS `__total_count__` \
                FROM `EMPLOYEE` LIMIT 40 OFFSET 120\
                """
            )
        ).affirm();
    }

    @Test
    void paginatesSelectWithWhereUsingLimitAndOffset() {
        new Assertion<>(
            "must paginate a select with where using LIMIT and OFFSET",
            () -> new MysqlPaginated(
                new QueryOf(
                    """
                    SELECT id, name, email FROM users WHERE status = 'active'
                    ORDER BY name
                    """
                ),
                1,
                10
            ).sql(),
            new IsText(
                """
                SELECT `ID`, `NAME`, `EMAIL`, COUNT(*) OVER () AS \
                `__total_count__` FROM `USERS` WHERE `STATUS` = 'active' \
                LIMIT 10 OFFSET 0\
                """
            )
        ).affirm();
    }

    @Test
    void paginatesGroupedSelectWithLimitAndOffset() {
        new Assertion<>(
            "must paginate a grouped select using LIMIT and OFFSET",
            () -> new MysqlPaginated(
                new QueryOf(
                    """
                    SELECT department, COUNT(*) as emp_count
                    FROM employees
                    GROUP BY department
                    ORDER BY emp_count DESC
                    """
                ),
                3,
                5
            ).sql(),
            new IsText(
                """
                SELECT *, COUNT(*) OVER () AS `__total_count__` FROM \
                (SELECT `DEPARTMENT`, COUNT(*) AS `EMP_COUNT` FROM \
                `EMPLOYEES` GROUP BY `DEPARTMENT`) AS \
                `__paginated_subquery__` ORDER BY `EMP_COUNT` DESC \
                LIMIT 5 OFFSET 10\
                """
            )
        ).affirm();
    }
}
