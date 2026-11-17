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
                SELECT *, COUNT(*) OVER () AS __total_count__ \
                FROM employee LIMIT 40 OFFSET 120\
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
                SELECT name, address, COUNT(*) OVER () AS \
                __total_count__ FROM employee LIMIT 30 OFFSET 120\
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
                SELECT id, name, email, COUNT(*) OVER () AS \
                __total_count__ FROM users WHERE status = 'active' \
                ORDER BY name LIMIT 10 OFFSET 0\
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
                SELECT u.id, u.name, u.email, o.order_date, \
                COUNT(*) OVER () AS __total_count__ FROM users AS u \
                LEFT JOIN orders AS o ON u.id = o.user_id WHERE \
                u.status = 'active' ORDER BY o.order_date DESC \
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
                SELECT *, COUNT(*) OVER () AS __total_count__ FROM \
                (SELECT department, COUNT(*) AS emp_count, AVG(salary) \
                AS avg_salary FROM employees WHERE \
                hire_date > '2023-01-01' GROUP BY department) AS \
                __paginated_subquery__ ORDER BY emp_count DESC LIMIT 5 \
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
                SELECT *, COUNT(*) OVER () AS __total_count__ FROM \
                (SELECT d.name AS department, COUNT(e.id) AS \
                employee_count, AVG(e.salary) AS avg_salary, \
                COUNT(e.id) OVER () AS total_employees, \
                AVG(e.salary) OVER (PARTITION BY d.id) AS \
                dept_avg_salary, (SELECT COUNT(*) FROM employee AS e2 \
                WHERE e2.department_id = d.id) AS \
                subquery_employee_count FROM department AS d LEFT JOIN \
                employee AS e ON e.department_id = d.id RIGHT \
                JOIN location AS l ON d.location_id = l.id FULL \
                JOIN company AS c ON l.company_id = c.id GROUP \
                BY d.id, d.name) AS __paginated_subquery__ ORDER \
                BY avg_salary DESC LIMIT 15 OFFSET 30\
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
                SELECT *, COUNT(*) OVER () AS __total_count__ FROM \
                (SELECT DISTINCT city FROM client) AS \
                __paginated_subquery__ LIMIT 7 OFFSET 14\
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
                SELECT *, COUNT(*) OVER () AS __total_count__ FROM \
                (SELECT DISTINCT city FROM client) AS \
                __paginated_subquery__ ORDER BY city LIMIT 4 OFFSET 4\
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

    @Test
    void keepsIdentifiersAsWritten() {
        new Assertion<>(
            "must keep identifiers in their original case and unquoted",
            () -> new Paginated(
                new QueryOf("SELECT Title, isbn FROM Book WHERE pages > 90"),
                2,
                25
            ).sql(),
            new IsText(
                """
                SELECT Title, isbn, COUNT(*) OVER () AS __total_count__ \
                FROM Book WHERE pages > 90 LIMIT 25 OFFSET 25\
                """
            )
        ).affirm();
    }

    @Test
    void keepsDoubleQuotedIdentifiers() {
        new Assertion<>(
            "must keep identifiers quoted with double quotes",
            () -> new Paginated(
                new QueryOf("SELECT \"Full Name\", age FROM \"Person\""),
                3,
                5
            ).sql(),
            new IsText(
                """
                SELECT "Full Name", age, COUNT(*) OVER () AS __total_count__ \
                FROM "Person" LIMIT 5 OFFSET 10\
                """
            )
        ).affirm();
    }

    @Test
    void paginatesBackTicksMixedWithDoubleQuotedStrings() {
        new Assertion<>(
            "must paginate back-ticked names mixed with double-quoted strings",
            () -> new Paginated(
                new QueryOf("SELECT `name` FROM `client` WHERE `name` = \"Ana\""),
                2,
                6
            ).sql(),
            new IsText(
                """
                SELECT `name`, COUNT(*) OVER () AS __total_count__ FROM \
                `client` WHERE `name` = 'Ana' LIMIT 6 OFFSET 6\
                """
            )
        ).affirm();
    }
}
