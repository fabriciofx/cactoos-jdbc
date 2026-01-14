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
                FROM `EMPLOYEE` OFFSET 120 ROWS FETCH NEXT 40 ROWS ONLY\
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
                `__total_count__` FROM `EMPLOYEE` OFFSET 120 ROWS FETCH NEXT \
                30 ROWS ONLY\
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
                OFFSET 0 ROWS FETCH NEXT 10 ROWS ONLY\
                """
            )
        );
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
                `U`.`STATUS` = 'active' OFFSET 20 ROWS FETCH NEXT 20 ROWS ONLY\
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
                `__paginated_subquery__` ORDER BY `EMP_COUNT` DESC OFFSET 0 \
                ROWS FETCH NEXT 5 ROWS ONLY\
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
                BY `AVG_SALARY` DESC OFFSET 30 ROWS FETCH NEXT 15 ROWS ONLY\
                """
            )
        ).affirm();
    }
}
