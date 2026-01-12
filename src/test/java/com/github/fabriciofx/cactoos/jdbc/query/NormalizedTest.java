/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.query.normalized.Normalized;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

/**
 * Normalized tests.
 *
 * @since 0.9.0
 */
@SuppressWarnings("PMD.TooManyMethods")
final class NormalizedTest {
    @Test
    void normalizeWithStart() throws Exception {
        new Assertion<>(
            "must normalize a select with star",
            () -> new Normalized(
                new QueryOf("SELECT * FROM person WHERE id = 1")
            ).sql(),
            new IsText("SELECT * FROM `PERSON` WHERE `ID` = 1")
        ).affirm();
    }

    @Test
    void normalizeWithWhere() throws Exception {
        new Assertion<>(
            "must normalize a select with where",
            () -> new Normalized(
                new QueryOf("SELECT id, name FROM person WHERE id = 1")
            ).sql(),
            new IsText("SELECT `ID`, `NAME` FROM `PERSON` WHERE `ID` = 1")
        ).affirm();
    }

    @Test
    void normalizeWithInvertedWhere() throws Exception {
        new Assertion<>(
            "must normalize a select with where",
            () -> new Normalized(
                new QueryOf("SELECT name, id FROM person WHERE 1 = id")
            ).sql(),
            new IsText("SELECT `ID`, `NAME` FROM `PERSON` WHERE `ID` = 1")
        ).affirm();
    }

    @Test
    void normalizeWithNoCommutativeWhere() throws Exception {
        new Assertion<>(
            "must normalize a select with no commutative where",
            () -> new Normalized(
                new QueryOf("SELECT * FROM t WHERE b = a - 1")
            ).sql(),
            new IsText("SELECT * FROM `T` WHERE `B` = `A` - 1")
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
            new IsText("SELECT `ID`, `NAME` FROM `PERSON` WHERE `ID` = ?")
        ).affirm();
    }

    @Test
    void normalizeOrderingColumns() throws Exception {
        new Assertion<>(
            "must normalize a select with where",
            () -> new Normalized(
                new QueryOf("SELECT d, b, c, a FROM person WHERE a = 1")
            ).sql(),
            new IsText("SELECT `A`, `B`, `C`, `D` FROM `PERSON` WHERE `A` = 1")
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
                SELECT `CARRIER`, `NAME`, `NUMBER` FROM `CONTACT` INNER \
                JOIN `PHONE` ON `CONTACT`.`ID` = `PHONE`.`CONTACT_ID` \
                WHERE `CONTACT`.`ID` = 123\
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
                SELECT `A`, `B`, `C`, `D` FROM `PERSON` \
                WHERE `A` = 1 AND `B` = 2\
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
                SELECT `A`, `B`, `C`, `D` FROM `PERSON` \
                WHERE `A` = 1 OR `B` = 2\
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
                SELECT `AGE`, `NAME` FROM `USERS` WHERE (`AGE` = 30 \
                OR `CITY` = 'NYC') AND `NAME` = 'John'\
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
                SELECT `AGE`, `CITY`, `NAME` FROM `USERS` WHERE `AGE` = 30 OR \
                `CITY` = 'NYC' OR `NAME` = 'John'\
                """
            )
        ).affirm();
    }

    @Test
    void normalizedAComplexQuery() throws Exception {
        new Assertion<>(
            "must normalize a complex query",
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
                WITH `ACTIVE_CUSTOMERS` AS (SELECT `C`.`COUNTRY`, `C`.`ID`, \
                `C`.`NAME`, `C`.`SEGMENT` FROM `CUSTOMERS` AS `C` WHERE \
                `C`.`STATUS` = 'ACTIVE'), `RECENT_ORDERS` AS (SELECT \
                `O`.`CREATED_AT`, `O`.`CUSTOMER_ID`, `O`.`ID` FROM `ORDERS` \
                AS `O` WHERE `O`.`CREATED_AT` >= DATE '2024-01-01') SELECT \
                `AC`.`COUNTRY`, `RO`.`CREATED_AT`, `AC`.`ID` AS `CUSTOMER_ID`, \
                `AC`.`NAME` AS `CUSTOMER_NAME`, `RO`.`ID` AS `ORDER_ID`, \
                `OI`.`PRODUCT_ID`, `OI`.`QUANTITY`, `AC`.`SEGMENT`, \
                `OI`.`UNIT_PRICE` FROM `ACTIVE_CUSTOMERS` AS `AC` INNER JOIN \
                `RECENT_ORDERS` AS `RO` ON `RO`.`CUSTOMER_ID` = `AC`.`ID` \
                INNER JOIN `ORDER_ITEMS` AS `OI` ON `OI`.`ORDER_ID` = \
                `RO`.`ID` WHERE EXISTS (SELECT 1 FROM `PRODUCTS` AS `P` \
                WHERE `P`.`ACTIVE` = TRUE AND `P`.`ID` = `OI`.`PRODUCT_ID`) \
                AND `AC`.`COUNTRY` = 'BR' AND `AC`.`SEGMENT` IN ('ENTERPRISE', \
                'SMB') AND `OI`.`UNIT_PRICE` > 100 ORDER BY `RO`.`CREATED_AT` \
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
                SELECT `DEPARTMENT`, `ID`, ROW_NUMBER() OVER (PARTITION BY \
                `DEPARTMENT` ORDER BY `SALARY` DESC) AS `RN`, `SALARY` FROM \
                `EMPLOYEES`\
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
                SELECT `DEPARTMENT`, `ID`, ROW_NUMBER() OVER (PARTITION BY \
                `DEPARTMENT` ORDER BY `SALARY` DESC) AS `RN`, `SALARY` \
                FROM `EMPLOYEES` QUALIFY `RN` = 1\
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
            new IsText("SELECT `A`, `B`, `C`, `D` FROM `T`")
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
                SELECT `A`, `B`, `C` FROM `P` ORDER BY `X` DESC FETCH NEXT \
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
                SELECT * FROM `USERS` WHERE `AGE` >= 30 AND `STATUS` = 'ACTIVE'\
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
            new IsText("SELECT * FROM `T`")
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
                SELECT `P`.`ID` FROM `PERSON` AS `P` INNER JOIN `ADDRESS` AS \
                `A` ON `A`.`PID` = `P`.`ID`\
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
            new IsText("SELECT * FROM `A` LEFT JOIN `B` ON `A`.`ID` = `B`.`ID`")
        ).affirm();
    }

    @Test
    void normalizePreservingDistinct() {
        new Assertion<>(
            "must normalize a query preserving distinct",
            () -> new Normalized(
                new QueryOf("SELECT DISTINCT country FROM person")
            ).sql(),
            new IsText("SELECT DISTINCT `COUNTRY` FROM `PERSON`")
        ).affirm();
    }

    @Test
    void normalizeRemovingComments() {
        new Assertion<>(
            "must normalize a query removing comments",
            () -> new Normalized(
                new QueryOf("SELECT /* test */ id FROM t -- comment")
            ).sql(),
            new IsText("SELECT `ID` FROM `T`")
        ).affirm();
    }

    @Test
    void normalizeWithInList() {
        new Assertion<>(
            "must normalize a query with in list",
            () -> new Normalized(
                new QueryOf("SELECT * FROM t WHERE id IN (3, 1, 2)")
            ).sql(),
            new IsText("SELECT * FROM `T` WHERE `ID` IN (3, 1, 2)")
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
                SELECT *, ROW_NUMBER() OVER (PARTITION BY `DEPT` ORDER BY \
                `SALARY` DESC) AS `RN` FROM `EMP`\
                """
            )
        ).affirm();
    }

    @Test
    void normalizeWithInnerLeftRightFullJoins() {
        new Assertion<>(
            "must normalize a query with inner, left, right, full joins",
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
                SELECT `O`.`ID`, `C`.`NAME`, `P`.`NAME` AS `PRODUCT_NAME`, \
                `R`.`REGION_NAME`, `S`.`NAME` AS `SUPPLIER_NAME` FROM `ORDERS` \
                AS `O` INNER JOIN `CUSTOMERS` AS `C` ON `C`.`ID` = \
                `O`.`CUSTOMER_ID` LEFT JOIN `PRODUCTS` AS `P` ON \
                `P`.`ID` = `O`.`PRODUCT_ID` RIGHT JOIN `SUPPLIERS` AS `S` ON \
                `S`.`ID` = `P`.`SUPPLIER_ID` FULL JOIN `REGIONS` AS `R` ON \
                `R`.`ID` = `S`.`REGION_ID` WHERE `C`.`ACTIVE` = TRUE AND \
                `O`.`CREATED_AT` >= DATE '2024-01-01'\
                """
            )
        ).affirm();
    }

    @Test
    void normalizeWithAggregateFunctions() throws Exception {
        new Assertion<>(
            "must normalize a query with aggregate functions",
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
                SELECT DISTINCT AVG(`SALARY`) AS `AVERAGE_SALARY`, \
                `DEPARTMENT`, COUNT(DISTINCT `ROLE`) AS `DISTINCT_ROLES`, \
                COUNT(`SALARY`) AS `EMPLOYEES_WITH_SALARY`, MAX(`SALARY`) AS \
                `HIGHEST_SALARY`, MIN(`SALARY`) AS `LOWEST_SALARY`, \
                PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY `SALARY`) AS \
                `MEDIAN_SALARY`, STDDEV_POP(`SALARY`) AS `STDDEV_POPULATION`, \
                STDDEV_SAMP(`SALARY`) AS `STDDEV_SAMPLE`, COUNT(*) AS \
                `TOTAL_EMPLOYEES`, SUM(`SALARY`) AS `TOTAL_SALARY`, \
                VAR_POP(`SALARY`) AS `VARIANCE_POPULATION`, \
                VAR_SAMP(`SALARY`) AS `VARIANCE_SAMPLE` FROM `EMPLOYEES` \
                WHERE `ACTIVE` = TRUE GROUP BY `DEPARTMENT` \
                HAVING AVG(`SALARY`) > 3000\
                """
            )
        ).affirm();
    }
}
