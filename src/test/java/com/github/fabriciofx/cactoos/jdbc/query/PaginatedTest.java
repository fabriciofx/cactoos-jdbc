/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

/**
 * Paginated tests.
 *
 * @since 0.8.0
 */
final class PaginatedTest {

    @Test
    void paginatedOnlyStart() {
        new Assertion<>(
            "must paginated a select with only star",
            () -> new Paginated(
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
    void paginatedFull() {
        new Assertion<>(
            "must paginated a select without where",
            () -> new Paginated(
                new QueryOf("SELECT name, address FROM employee"),
                5,
                30
            ).sql(),
            new IsText(
                """
                SELECT `NAME`, `ADDRESS`, COUNT(*) OVER () AS \
                `__total_count__` FROM `EMPLOYEE` LIMIT 30 OFFSET 120\
                """
            )
        ).affirm();
    }

    @Test
    void paginatedWhereAndOrderBy() {
        new Assertion<>(
            "must paginated a select with where and order by",
            () -> new Paginated(
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
                ORDER BY `NAME` LIMIT 10 OFFSET 0\
                """
            )
        ).affirm();
    }

    @Test
    void paginatedLeftJoinWhereAndOrderBy() {
        new Assertion<>(
            "must paginated a select with left join, where and order by",
            () -> new Paginated(
                new QueryOf(
                    """
                    SELECT u.id, u.name, u.email, o.order_date
                    FROM users u
                    LEFT JOIN orders o ON u.id = o.user_id
                    WHERE u.status = 'active'
                    ORDER BY o.order_date
                    DESC
                    """
                ),
                2,
                20
            ).sql(),
            new IsText(
                """
                SELECT `U`.`ID`, `U`.`NAME`, `U`.`EMAIL`, `O`.`ORDER_DATE`, \
                COUNT(*) OVER () AS `__total_count__` FROM `USERS` AS `U` \
                LEFT JOIN `ORDERS` AS `O` ON `U`.`ID` = `O`.`USER_ID` WHERE \
                `U`.`STATUS` = 'active' ORDER BY `O`.`ORDER_DATE` DESC \
                LIMIT 20 OFFSET 20\
                """
            )
        ).affirm();
    }

    @Test
    void paginatedWhereGroupByAndOrderBy() {
        new Assertion<>(
            "must paginated a select with where, group by and order by",
            () -> new Paginated(
                new QueryOf(
                    """
                    SELECT department, COUNT(*) as emp_count,
                        AVG(salary) as avg_salary
                    FROM employees
                    WHERE hire_date > '2023-01-01'
                    GROUP BY department
                    ORDER BY emp_count DESC
                    """
                ),
                1,
                5
            ).sql(),
            new IsText(
                """
                SELECT *, COUNT(*) OVER () AS `__total_count__` FROM \
                (SELECT `DEPARTMENT`, COUNT(*) AS `EMP_COUNT`, AVG(`SALARY`) \
                AS `AVG_SALARY` FROM `EMPLOYEES` WHERE \
                `HIRE_DATE` > '2023-01-01' GROUP BY `DEPARTMENT`) AS \
                `__paginated_subquery__` ORDER BY `EMP_COUNT` DESC LIMIT 5 \
                OFFSET 0\
                """
            )
        ).affirm();
    }

    @Test
    void paginatedCountAvgSubqueryWindowLeftRightFullJoinGroupByAndOrderBy() {
        new Assertion<>(
            """
            must paginated a query with count, avg, subquery, window, left, \
            right, full join, group by and order by\
            """,
            // @checkstyle LambdaBodyLengthCheck (50 lines)
            () -> new Paginated(
                new QueryOf(
                    """
                    SELECT
                        d.name AS department,
                        COUNT(e.id) AS employee_count,
                        AVG(e.salary) AS avg_salary,
                        -- Window functions
                        COUNT(e.id) OVER () AS total_employees,
                        AVG(e.salary) OVER (PARTITION BY d.id) AS dept_avg_salary,
                        -- Subquery no SELECT
                        (
                            SELECT COUNT(*)
                            FROM employee e2
                            WHERE e2.department_id = d.id
                        ) AS subquery_employee_count
                    FROM department d
                    -- LEFT JOIN
                    LEFT JOIN employee e
                        ON e.department_id = d.id
                    -- RIGHT JOIN
                    RIGHT JOIN location l
                        ON d.location_id = l.id
                    -- FULL JOIN
                    FULL JOIN company c
                        ON l.company_id = c.id
                    GROUP BY
                        d.id,
                        d.name
                    ORDER BY
                        avg_salary DESC
                    """
                ),
                3,
                15
            ).sql(),
            new IsText(
                """
                SELECT *, COUNT(*) OVER () AS `__total_count__` FROM \
                (SELECT `D`.`NAME` AS `DEPARTMENT`, COUNT(`E`.`ID`) AS \
                `EMPLOYEE_COUNT`, AVG(`E`.`SALARY`) AS `AVG_SALARY`, \
                COUNT(`E`.`ID`) OVER () AS `TOTAL_EMPLOYEES`, \
                AVG(`E`.`SALARY`) OVER (PARTITION BY `D`.`ID`) AS \
                `DEPT_AVG_SALARY`, (SELECT COUNT(*) FROM `EMPLOYEE` AS `E2` \
                WHERE `E2`.`DEPARTMENT_ID` = `D`.`ID`) AS \
                `SUBQUERY_EMPLOYEE_COUNT` FROM `DEPARTMENT` AS `D` LEFT JOIN \
                `EMPLOYEE` AS `E` ON `E`.`DEPARTMENT_ID` = `D`.`ID` RIGHT \
                JOIN `LOCATION` AS `L` ON `D`.`LOCATION_ID` = `L`.`ID` FULL \
                JOIN `COMPANY` AS `C` ON `L`.`COMPANY_ID` = `C`.`ID` GROUP \
                BY `D`.`ID`, `D`.`NAME`) AS `__paginated_subquery__` ORDER \
                BY `AVG_SALARY` DESC LIMIT 15 OFFSET 30\
                """
            )
        ).affirm();
    }

    @Test
    void keepsSelectAlreadyLimited() {
        new Assertion<>(
            "must not paginate again a select that already has LIMIT",
            () -> new Paginated(
                new QueryOf("SELECT name FROM planet LIMIT 9 OFFSET 27"),
                3,
                11
            ).sql(),
            new IsText("SELECT name FROM planet LIMIT 9 OFFSET 27")
        ).affirm();
    }

    @Test
    void keepsOrderedSelectAlreadyLimited() {
        new Assertion<>(
            "must not paginate again an ordered select that already has LIMIT",
            () -> new Paginated(
                new QueryOf("SELECT name FROM moon ORDER BY name LIMIT 4"),
                2,
                17
            ).sql(),
            new IsText("SELECT name FROM moon ORDER BY name LIMIT 4")
        ).affirm();
    }

    @Test
    void keepsSelectAlreadyFetched() {
        new Assertion<>(
            "must not paginate again a select that already has FETCH",
            () -> new Paginated(
                new QueryOf(
                    "SELECT id FROM star OFFSET 6 ROWS FETCH NEXT 2 ROWS ONLY"
                ),
                5,
                8
            ).sql(),
            new IsText(
                "SELECT id FROM star OFFSET 6 ROWS FETCH NEXT 2 ROWS ONLY"
            )
        ).affirm();
    }

    @Test
    void paginatesDistinctSelectInSubquery() {
        new Assertion<>(
            "must keep DISTINCT and count only the distinct rows",
            () -> new Paginated(
                new QueryOf("SELECT DISTINCT city FROM client"),
                3,
                7
            ).sql(),
            new IsText(
                """
                SELECT *, COUNT(*) OVER () AS `__total_count__` FROM \
                (SELECT DISTINCT `CITY` FROM `CLIENT`) AS \
                `__paginated_subquery__` LIMIT 7 OFFSET 14\
                """
            )
        ).affirm();
    }

    @Test
    void paginatesOrderedDistinctSelectInSubquery() {
        new Assertion<>(
            "must keep DISTINCT and ORDER BY and count only the distinct rows",
            () -> new Paginated(
                new QueryOf("SELECT DISTINCT city FROM client ORDER BY city"),
                2,
                4
            ).sql(),
            new IsText(
                """
                SELECT *, COUNT(*) OVER () AS `__total_count__` FROM \
                (SELECT DISTINCT `CITY` FROM `CLIENT`) AS \
                `__paginated_subquery__` ORDER BY `CITY` LIMIT 4 OFFSET 4\
                """
            )
        ).affirm();
    }

    @Test
    void keepsSelectLimitedWithOffsetAndCount() {
        new Assertion<>(
            "must not paginate again a select with LIMIT offset, count",
            () -> new Paginated(
                new QueryOf("SELECT name FROM crater LIMIT 15, 5"),
                6,
                12
            ).sql(),
            new IsText("SELECT name FROM crater LIMIT 15, 5")
        ).affirm();
    }
}
