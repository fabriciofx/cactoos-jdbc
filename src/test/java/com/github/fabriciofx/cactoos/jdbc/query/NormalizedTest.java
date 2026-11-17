/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

/**
 * Normalized tests.
 *
 * @since 0.9.0
 */
final class NormalizedTest {

    @Test
    void normalizeWithStart() throws Exception {
        new Assertion<>(
            "must normalize a select with star",
            () -> new Normalized(
                new QueryOf("SELECT * FROM person WHERE id = 1")
            ).sql(),
            new IsText("SELECT * FROM person WHERE id = 1")
        ).affirm();
    }

    @Test
    void normalizeWithWhere() throws Exception {
        new Assertion<>(
            "must normalize a select with where",
            () -> new Normalized(
                new QueryOf("SELECT id, name FROM person WHERE id = 1")
            ).sql(),
            new IsText("SELECT id, name FROM person WHERE id = 1")
        ).affirm();
    }

    @Test
    void normalizeWithInvertedWhere() throws Exception {
        new Assertion<>(
            "must normalize a select with where",
            () -> new Normalized(
                new QueryOf("SELECT name, id FROM person WHERE 1 = id")
            ).sql(),
            new IsText("SELECT id, name FROM person WHERE id = 1")
        ).affirm();
    }

    @Test
    void normalizeWithNoCommutativeWhere() throws Exception {
        new Assertion<>(
            "must normalize a select with no commutative where",
            () -> new Normalized(
                new QueryOf("SELECT * FROM t WHERE b = a - 1")
            ).sql(),
            new IsText("SELECT * FROM t WHERE b = a - 1")
        ).affirm();
    }

    @Test
    void normalizeANamedQuery() throws Exception {
        new Assertion<>(
            "must normalize a named select with where",
            () -> new Normalized(
                new NamedQuery(
                    "SELECT id, name FROM person WHERE id = :id",
                    new IntParam("id", 1)
                )
            ).sql(),
            new IsText("SELECT id, name FROM person WHERE id = ?")
        ).affirm();
    }

    @Test
    void normalizeOrderingColumns() throws Exception {
        new Assertion<>(
            "must normalize a select with where",
            () -> new Normalized(
                new QueryOf("SELECT d, b, c, a FROM person WHERE a = 1")
            ).sql(),
            new IsText("SELECT a, b, c, d FROM person WHERE a = 1")
        ).affirm();
    }

    @Test
    void normalizeWithWhereAndBacksticks() {
        new Assertion<>(
            "must normalize a select with where and backsticks",
            () -> new Normalized(
                new QueryOf("SELECT `name` FROM `person` WHERE `id` = 1")
            ).sql(),
            new IsText("SELECT `name` FROM `person` WHERE `id` = 1")
        ).affirm();
    }

    @Test
    void normalizeWithJoins() {
        new Assertion<>(
            "must normalize a select with joins",
            () -> new Normalized(
                new QueryOf(
                    """
                    SELECT name, number, carrier FROM contact INNER JOIN
                    phone ON contact.id = phone.contact_id WHERE
                    contact.id = 123
                    """
                )
            ).sql(),
            new IsText(
                """
                SELECT carrier, name, number FROM contact INNER \
                JOIN phone ON contact.id = phone.contact_id \
                WHERE contact.id = 123\
                """
            )
        ).affirm();
    }

    @Test
    void normalizeWithChangingWhereWithAnd() {
        new Assertion<>(
            "must normalize a select with changing where with and",
            () -> new Normalized(
                new QueryOf(
                    "SELECT d, a, b, c FROM `PERSON` WHERE b = 2 and a = 1"
                )
            ).sql(),
            new IsText(
                """
                SELECT a, b, c, d FROM `PERSON` \
                WHERE a = 1 AND b = 2\
                """
            )
        ).affirm();
    }

    @Test
    void normalizeWithChangingWhereWithOr() {
        new Assertion<>(
            "must normalize a select with changing where with or",
            () -> new Normalized(
                new QueryOf(
                    "SELECT d, a, b, c FROM `PERSON` WHERE b = 2 or a = 1"
                )
            ).sql(),
            new IsText(
                """
                SELECT a, b, c, d FROM `PERSON` \
                WHERE a = 1 OR b = 2\
                """
            )
        ).affirm();
    }

    @Test
    void normalizeWithChangingWhereWithAndAndOr() {
        new Assertion<>(
            "must normalize a select with changing where with and and or",
            () -> new Normalized(
                new QueryOf(
                    """
                    SELECT name, age FROM users WHERE (age = 30 OR
                    city = 'NYC') AND name = 'John'
                    """
                )
            ).sql(),
            new IsText(
                """
                SELECT age, name FROM users WHERE (age = 30 \
                OR city = 'NYC') AND name = 'John'\
                """
            )
        ).affirm();
    }

    @Test
    void normalizeWithChangingWhereWithMultipleOr() {
        new Assertion<>(
            "must normalize a select with changing where with multiple or",
            () -> new Normalized(
                new QueryOf(
                    """
                    SELECT name, age, city FROM users WHERE name = 'John' OR
                    city = 'NYC' OR age = 30
                    """
                )
            ).sql(),
            new IsText(
                """
                SELECT age, city, name FROM users WHERE age = 30 OR \
                city = 'NYC' OR name = 'John'\
                """
            )
        ).affirm();
    }

    @Test
    void normalizedAComplexQuery() throws Exception {
        new Assertion<>(
            "must normalize a complex query",
            // @checkstyle LambdaBodyLengthCheck (50 lines)
            () -> new Normalized(
                new QueryOf(
                    """
                    WITH active_customers AS (
                        SELECT
                            c.id,
                            c.name,
                            c.country,
                            c.segment
                        FROM customers c
                        WHERE c.status = 'ACTIVE'
                    ),
                    recent_orders AS (
                        SELECT
                            o.id,
                            o.customer_id,
                            o.created_at
                        FROM orders o
                        WHERE o.created_at >= DATE '2024-01-01'
                    )
                    SELECT
                        ac.id AS customer_id,
                        ac.name AS customer_name,
                        ac.country,
                        ac.segment,
                        ro.id AS order_id,
                        ro.created_at,
                        oi.product_id,
                        oi.quantity,
                        oi.unit_price
                    FROM active_customers ac
                    JOIN recent_orders ro
                        ON ro.customer_id = ac.id
                    JOIN order_items oi
                        ON oi.order_id = ro.id
                    WHERE ac.country = 'BR'
                        AND ac.segment IN ('ENTERPRISE', 'SMB')
                        AND oi.unit_price > 100
                        AND EXISTS (
                            SELECT 1
                            FROM products p
                            WHERE p.id = oi.product_id
                            AND p.active = TRUE
                        )
                    ORDER BY ro.created_at DESC
                    OFFSET 20 ROWS
                    FETCH FIRST 50 ROWS ONLY
                    """
                )
            ).sql(),
            new IsText(
                """
                WITH active_customers AS (SELECT c.country, c.id, \
                c.name, c.segment FROM customers AS c WHERE \
                c.status = 'ACTIVE'), recent_orders AS (SELECT \
                o.created_at, o.customer_id, o.id FROM orders \
                AS o WHERE o.created_at >= DATE '2024-01-01') SELECT \
                ac.country, ro.created_at, ac.id AS customer_id, \
                ac.name AS customer_name, ro.id AS order_id, \
                oi.product_id, oi.quantity, ac.segment, \
                oi.unit_price FROM active_customers AS ac INNER JOIN \
                recent_orders AS ro ON ro.customer_id = ac.id \
                INNER JOIN order_items AS oi ON oi.order_id = \
                ro.id WHERE EXISTS (SELECT 1 FROM products AS p \
                WHERE p.active = TRUE AND p.id = oi.product_id) \
                AND ac.country = 'BR' AND ac.segment IN ('ENTERPRISE', \
                'SMB') AND oi.unit_price > 100 ORDER BY ro.created_at \
                DESC OFFSET 20 ROWS FETCH NEXT 50 ROWS ONLY\
                """
            )
        ).affirm();
    }

    @Test
    void normalizeWithRowNumber() throws Exception {
        new Assertion<>(
            "must normalize a query with row_number()",
            () -> new Normalized(
                new QueryOf(
                    """
                    SELECT
                        id,
                        department,
                        salary,
                    ROW_NUMBER() OVER (
                        PARTITION BY department
                        ORDER BY salary DESC
                    ) AS rn
                    FROM employees
                    """
                )
            ).sql(),
            new IsText(
                """
                SELECT department, id, ROW_NUMBER() OVER (PARTITION BY \
                department ORDER BY salary DESC) AS rn, salary FROM \
                employees\
                """
            )
        ).affirm();
    }

    @Test
    void normalizeWithRowNumberAndQualify() throws Exception {
        new Assertion<>(
            "must normalized a query with row_number() and qualify",
            () -> new Normalized(
                new QueryOf(
                    """
                    SELECT
                        id,
                        department,
                        salary,
                    ROW_NUMBER() OVER (
                        PARTITION BY department
                        ORDER BY salary DESC
                    ) AS rn
                    FROM employees
                    QUALIFY rn = 1
                    """
                )
            ).sql(),
            new IsText(
                """
                SELECT department, id, ROW_NUMBER() OVER (PARTITION BY \
                department ORDER BY salary DESC) AS rn, salary \
                FROM employees QUALIFY rn = 1\
                """
            )
        ).affirm();
    }

    @Test
    void normalizeWithOrderByNotSemantic() {
        new Assertion<>(
            "must normalize a query with order by not semantic",
            () -> new Normalized(
                new QueryOf("SELECT d, c, b, a FROM t ORDER BY b, a")
            ).sql(),
            new IsText("SELECT a, b, c, d FROM t")
        ).affirm();
    }

    @Test
    void normalizeWithOrderBySemantic() {
        new Assertion<>(
            "must normalize a query with order by semantic",
            () -> new Normalized(
                new QueryOf("SELECT b, a, c FROM p ORDER BY x DESC LIMIT 10")
            ).sql(),
            new IsText(
                """
                SELECT a, b, c FROM p ORDER BY x DESC FETCH NEXT \
                10 ROWS ONLY\
                """
            )
        ).affirm();
    }

    @Test
    void normalizeWithOrderBySemanticAndLogicOperator() {
        new Assertion<>(
            "must normalize a query with order by semantic and logic operator",
            () -> new Normalized(
                new QueryOf(
                    "SELECT * FROM users WHERE 'ACTIVE' = status AND 30 <= age"
                )
            ).sql(),
            new IsText(
                """
                SELECT * FROM users WHERE age >= 30 AND status = 'ACTIVE'\
                """
            )
        ).affirm();
    }

    @Test
    void normalizeRemovingOptionalAsc() {
        new Assertion<>(
            "must normalize a query removing optional asc",
            () -> new Normalized(
                new QueryOf("SELECT * FROM t ORDER BY a ASC")
            ).sql(),
            new IsText("SELECT * FROM t")
        ).affirm();
    }

    @Test
    void normalizeWithDifferentAliases() {
        new Assertion<>(
            "must normalize a query with different aliases",
            () -> new Normalized(
                new QueryOf(
                    "SELECT p.id FROM person p JOIN address a ON a.pid = p.id"
                )
            ).sql(),
            new IsText(
                """
                SELECT p.id FROM person AS p INNER JOIN address AS \
                a ON a.pid = p.id\
                """
            )
        ).affirm();
    }

    @Test
    void normalizeWithLeftJoin() {
        new Assertion<>(
            "must normalize a query with left join",
            () -> new Normalized(
                new QueryOf("SELECT * FROM a LEFT JOIN b ON a.id = b.id")
            ).sql(),
            new IsText("SELECT * FROM a LEFT JOIN b ON a.id = b.id")
        ).affirm();
    }

    @Test
    void normalizePreservingDistinct() {
        new Assertion<>(
            "must normalize a query preserving distinct",
            () -> new Normalized(
                new QueryOf("SELECT DISTINCT country FROM person")
            ).sql(),
            new IsText("SELECT DISTINCT country FROM person")
        ).affirm();
    }

    @Test
    void normalizeRemovingComments() {
        new Assertion<>(
            "must normalize a query removing comments",
            () -> new Normalized(
                new QueryOf("SELECT /* test */ id FROM t -- comment")
            ).sql(),
            new IsText("SELECT id FROM t")
        ).affirm();
    }

    @Test
    void normalizeWithInList() {
        new Assertion<>(
            "must normalize a query with in list",
            () -> new Normalized(
                new QueryOf("SELECT * FROM t WHERE id IN (3, 1, 2)")
            ).sql(),
            new IsText("SELECT * FROM t WHERE id IN (3, 1, 2)")
        ).affirm();
    }

    @Test
    void normalizeWithWindowList() {
        new Assertion<>(
            "must normalize a query with window list",
            () -> new Normalized(
                new QueryOf(
                    """
                    SELECT *, ROW_NUMBER() OVER (PARTITION BY dept ORDER BY
                    salary DESC) rn FROM emp
                    """
                )
            ).sql(),
            new IsText(
                """
                SELECT *, ROW_NUMBER() OVER (PARTITION BY dept ORDER BY \
                salary DESC) AS rn FROM emp\
                """
            )
        ).affirm();
    }

    @Test
    void normalizeWithInnerLeftRightFullJoins() {
        new Assertion<>(
            "must normalize a query with inner, left, right, full joins",
            // @checkstyle LambdaBodyLengthCheck (50 lines)
            () -> new Normalized(
                new QueryOf(
                    """
                    SELECT
                        o.id,
                        c.name,
                        p.name AS product_name,
                        s.name AS supplier_name,
                        r.region_name
                    FROM orders o
                    INNER JOIN customers c
                        ON c.id = o.customer_id
                    LEFT JOIN products p
                        ON p.id = o.product_id
                    RIGHT JOIN suppliers s
                        ON s.id = p.supplier_id
                    FULL OUTER JOIN regions r
                        ON r.id = s.region_id
                    WHERE
                        o.created_at >= DATE '2024-01-01'
                        AND c.active = TRUE
                    """
                )
            ).sql(),
            new IsText(
                """
                SELECT o.id, c.name, p.name AS product_name, \
                r.region_name, s.name AS supplier_name FROM orders \
                AS o INNER JOIN customers AS c ON c.id = \
                o.customer_id LEFT JOIN products AS p ON \
                p.id = o.product_id RIGHT JOIN suppliers AS s ON \
                s.id = p.supplier_id FULL JOIN regions AS r ON \
                r.id = s.region_id WHERE c.active = TRUE AND \
                o.created_at >= DATE '2024-01-01'\
                """
            )
        ).affirm();
    }

    @Test
    void normalizeWithAggregateFunctions() throws Exception {
        new Assertion<>(
            "must normalize a query with aggregate functions",
            // @checkstyle LambdaBodyLengthCheck (50 lines)
            () -> new Normalized(
                new QueryOf(
                    """
                    SELECT
                        department,
                        COUNT(*) AS total_employees,
                        COUNT(salary) AS employees_with_salary,
                        COUNT(DISTINCT role) AS distinct_roles,
                        SUM(salary) AS total_salary,
                        AVG(salary) AS average_salary,
                        MIN(salary) AS lowest_salary,
                        MAX(salary) AS highest_salary,
                        VAR_POP(salary) AS variance_population,
                        VAR_SAMP(salary) AS variance_sample,
                        STDDEV_POP(salary) AS stddev_population,
                        STDDEV_SAMP(salary) AS stddev_sample,
                        PERCENTILE_CONT(0.5)
                    WITHIN GROUP (ORDER BY salary) AS median_salary
                    FROM employees
                    WHERE active = TRUE
                    GROUP BY department
                    HAVING AVG(salary) > 3000
                    ORDER BY average_salary DESC
                    """
                )
            ).sql(),
            new IsText(
                """
                SELECT DISTINCT AVG(salary) AS average_salary, \
                department, COUNT(DISTINCT role) AS distinct_roles, \
                COUNT(salary) AS employees_with_salary, MAX(salary) AS \
                highest_salary, MIN(salary) AS lowest_salary, \
                PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY salary) AS \
                median_salary, STDDEV_POP(salary) AS stddev_population, \
                STDDEV_SAMP(salary) AS stddev_sample, COUNT(*) AS \
                total_employees, SUM(salary) AS total_salary, \
                VAR_POP(salary) AS variance_population, \
                VAR_SAMP(salary) AS variance_sample FROM employees \
                WHERE active = TRUE GROUP BY department \
                HAVING AVG(salary) > 3000\
                """
            )
        ).affirm();
    }

    @Test
    void normalizesLimitWithOffsetAndCount() throws Exception {
        new Assertion<>(
            "must normalize a select with LIMIT offset, count",
            () -> new Normalized(
                new QueryOf("SELECT b, a FROM dune ORDER BY a LIMIT 30, 6")
            ).sql(),
            new IsText(
                """
                SELECT a, b FROM dune ORDER BY a OFFSET 30 ROWS FETCH \
                NEXT 6 ROWS ONLY\
                """
            )
        ).affirm();
    }

    @Test
    void normalizesLimitWithoutOrderBy() throws Exception {
        new Assertion<>(
            "must not print an empty ORDER BY for a select with only LIMIT",
            () -> new Normalized(
                new QueryOf("SELECT y, x FROM glacier LIMIT 4")
            ).sql(),
            new IsText("SELECT x, y FROM glacier FETCH NEXT 4 ROWS ONLY")
        ).affirm();
    }

    @Test
    void normalizesOffsetFetchWithoutOrderBy() throws Exception {
        new Assertion<>(
            "must not print an empty ORDER BY for a select with only FETCH",
            () -> new Normalized(
                new QueryOf(
                    "SELECT q FROM fjord OFFSET 9 ROWS FETCH NEXT 3 ROWS ONLY"
                )
            ).sql(),
            new IsText(
                "SELECT q FROM fjord OFFSET 9 ROWS FETCH NEXT 3 ROWS ONLY"
            )
        ).affirm();
    }

    @Test
    void keepsIdentifiersAsWritten() throws Exception {
        new Assertion<>(
            "must keep identifiers in their original case and unquoted",
            () -> new Normalized(
                new QueryOf("SELECT vat, Amount FROM Invoice WHERE vat > 7")
            ).sql(),
            new IsText("SELECT Amount, vat FROM Invoice WHERE vat > 7")
        ).affirm();
    }
}
