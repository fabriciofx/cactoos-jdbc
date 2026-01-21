/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.sql;

import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import org.apache.calcite.sql.SqlKind;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * QueryKind tests.
 *
 * @since 0.9.0
 */
@SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
final class QueryKindTest {
    @Test
    void checkIfAStatementIsASelect() {
        new Assertion<>(
            "must check if statement is a select",
            () -> new QueryKind(
                new QueryOf("SELECT id, name FROM person")
            ).value(),
            new HasValue<>(SqlKind.SELECT)
        ).affirm();
    }

    @Test
    void checkIfAStatementIsACreateTable() {
        new Assertion<>(
            "must check if statement is a create table",
            () -> new QueryKind(
                new QueryOf(
                    """
                    CREATE TABLE person (id INT, name VARCHAR(30) ,
                    PRIMARY KEY (id))
                    """
                )
            ).value(),
            new HasValue<>(SqlKind.CREATE_TABLE)
        ).affirm();
    }

    @Test
    void checkIfAStatementIsSimpleCreateTable() {
        new Assertion<>(
            "must check if statement is a simple create table",
            () -> new QueryKind(
                new QueryOf(
                    "CREATE TABLE emp (id INT)"
                )
            ).value(),
            new HasValue<>(SqlKind.CREATE_TABLE)
        ).affirm();
    }

    @Test
    void checkIfAStatementIsUpdate() {
        new Assertion<>(
            "must check if statement is an update",
            () -> new QueryKind(
                new QueryOf(
                    "UPDATE emp SET salary = 5000 WHERE id = 1"
                )
            ).value(),
            new HasValue<>(SqlKind.UPDATE)
        ).affirm();
    }

    @Test
    void checkIfIsASelectWithCommonTableExpression() {
        new Assertion<>(
            "must check if statement is a select with common table expression",
            () -> new QueryKind(
                new QueryOf(
                    """
                    WITH recent_users AS (SELECT * FROM users WHERE
                    created_at > CURRENT_TIMESTAMP - INTERVAL '7' DAY)
                    SELECT * FROM recent_users
                    """
                )
            ).value(),
            new HasValue<>(SqlKind.SELECT)
        ).affirm();
    }

    @Test
    void checkIfIsAInsertWithCommonTableExpression() {
        new Assertion<>(
            "must check if statement is an insert with common table expression",
            () -> new QueryKind(
                new QueryOf(
                    """
                    INSERT INTO users (name, email) SELECT name, email
                    FROM temp_users WHERE active = true
                    """
                )
            ).value(),
            new HasValue<>(SqlKind.INSERT)
        ).affirm();
    }

    @Test
    void checkIfIsADelete() {
        new Assertion<>(
            "must check if statement is a delete",
            () -> new QueryKind(
                new QueryOf(
                    """
                    DELETE FROM orders
                    WHERE customer_id IN (
                        SELECT customer_id
                        FROM customers
                        WHERE region = 'North'
                        AND status = 'inactive'
                        AND customer_id NOT IN (
                            SELECT customer_id
                            FROM orders
                            WHERE order_date > CURRENT_DATE - 30
                        )
                    )
                    AND order_total < (
                        SELECT AVG(order_total)
                        FROM orders
                        WHERE order_date > CURRENT_DATE - 90
                    )
                    AND EXISTS (
                        SELECT 1
                        FROM order_items oi
                        WHERE oi.order_id = orders.id
                        AND oi.quantity > 10
                    )
                    """
                )
            ).value(),
            new HasValue<>(SqlKind.DELETE)
        ).affirm();
    }
}
