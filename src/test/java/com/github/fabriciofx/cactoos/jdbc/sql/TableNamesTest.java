/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.sql;

import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import org.cactoos.list.ListOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValues;
import org.llorllale.cactoos.matchers.Matches;

/**
 * TableNames tests.
 * @since 0.9.0
 * @checkstyle AvoidDuplicateLiterals (500 lines)
 */
@SuppressWarnings({"PMD.UnitTestShouldIncludeAssert", "PMD.AvoidDuplicateLiterals"})
final class TableNamesTest {
    @Test
    void tableNamesSelectWhere() throws Exception {
        new Assertion<>(
            "must get table names from select with where",
            new HasValues<>(
                new TableNames(
                    new QueryOf("SELECT * FROM users WHERE status = 'active'")
                ).value()
            ),
            new Matches<>(new ListOf<>("USERS"))
        ).affirm();
    }

    @Test
    void tableNamesSelectJoin() throws Exception {
        new Assertion<>(
            "must get table names from select with join",
            new HasValues<>(
                new TableNames(
                    new QueryOf(
                        """
                        SELECT u.name, o.total FROM users u INNER JOIN orders \
                        o ON u.id = o.user_id\
                        """
                    )
                ).value()
            ),
            new Matches<>(new ListOf<>("USERS", "ORDERS"))
        ).affirm();
    }

    @Test
    void tableNamesSelectSub() throws Exception {
        new Assertion<>(
            "must get table names from select with subquery",
            new HasValues<>(
                new TableNames(
                    new QueryOf(
                        """
                        SELECT * FROM users WHERE id IN (SELECT user_id FROM \
                        orders WHERE total > 1000)
                        """
                    )
                ).value()
            ),
            new Matches<>(new ListOf<>("USERS", "ORDERS"))
        ).affirm();
    }

    @Test
    void tableNamesInsert() throws Exception {
        new Assertion<>(
            "must get table names from insert",
            new HasValues<>(
                new TableNames(
                    new QueryOf(
                        """
                        INSERT INTO users (name, email) VALUES ('John', \
                        'john@example.com')
                        """
                    )
                ).value()
            ),
            new Matches<>(new ListOf<>("USERS"))
        ).affirm();
    }

    @Test
    void tableNamesInsertJoin() throws Exception {
        new Assertion<>(
            "must get table names from insert with join",
            new HasValues<>(
                new TableNames(
                    new QueryOf(
                        """
                        INSERT INTO order_summary (user_id, total_orders) \
                        SELECT u.id, COUNT(o.id) FROM users u LEFT JOIN \
                        orders o ON u.id = o.user_id GROUP BY u.id\
                        """
                    )
                ).value()
            ),
            new Matches<>(new ListOf<>("ORDER_SUMMARY", "USERS", "ORDERS"))
        ).affirm();
    }

    @Test
    void tableNamesUpdate() throws Exception {
        new Assertion<>(
            "must get table names from update",
            new HasValues<>(
                new TableNames(
                    new QueryOf(
                        """
                        UPDATE users SET status = 'inactive' WHERE last_login \
                        < '2024-01-01'
                        """
                    )
                ).value()
            ),
            new Matches<>(new ListOf<>("USERS"))
        ).affirm();
    }

    @Test
    void tableNamesUpdateSub() throws Exception {
        new Assertion<>(
            "must get table names from update with subquery",
            new HasValues<>(
                new TableNames(
                    new QueryOf(
                        """
                        UPDATE users SET status = 'premium' WHERE id IN \
                        (SELECT user_id FROM orders GROUP BY user_id \
                        HAVING SUM(total) > 10000)\
                        """
                    )
                ).value()
            ),
            new Matches<>(new ListOf<>("USERS", "ORDERS"))
        ).affirm();
    }

    @Test
    void tableNamesDelete() throws Exception {
        new Assertion<>(
            "must get table names from delete",
            new HasValues<>(
                new TableNames(
                    new QueryOf("DELETE FROM users WHERE status = 'deleted'")
                ).value()
            ),
            new Matches<>(new ListOf<>("USERS"))
        ).affirm();
    }

    @Test
    void tableNamesDeleteSub() throws Exception {
        new Assertion<>(
            "must get table names from delete with subquery",
            new HasValues<>(
                new TableNames(
                    new QueryOf(
                        """
                        DELETE FROM order_items WHERE order_id IN (SELECT id \
                        FROM orders WHERE status = 'cancelled')\
                        """
                    )
                ).value()
            ),
            new Matches<>(new ListOf<>("ORDER_ITEMS", "ORDERS"))
        ).affirm();
    }

    @Test
    void tableNamesCteSelect() throws Exception {
        new Assertion<>(
            "must get table names from cte with select",
            new HasValues<>(
                new TableNames(
                    new QueryOf(
                        """
                        WITH recent_orders AS (SELECT user_id, COUNT(*) as \
                        order_count FROM orders WHERE order_date > \
                        CURRENT_DATE - INTERVAL '30' DAY GROUP BY user_id) \
                        SELECT u.id, u.name, ro.order_count FROM users u \
                        INNER JOIN recent_orders ro ON u.id = ro.user_id \
                        WHERE ro.order_count >= 5\
                        """
                    )
                ).value()
            ),
            new Matches<>(new ListOf<>("RECENT_ORDERS", "ORDERS", "USERS"))
        ).affirm();
    }
}
