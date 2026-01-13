/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache.cacheability;

import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsTrue;

/**
 * CacheableTest.
 * @since 0.9.0
 */
final class CacheableTest {
    @Test
    void notCacheSelectStar() throws Exception {
        new Assertion<>(
            "must not cache a select star",
            new Cacheable(
                new QueryOf("SELECT * FROM person")
            ).value(),
            new IsNot<>(new IsTrue())
        ).affirm();
    }

    @Test
    void cacheSelectWithWhere() throws Exception {
        new Assertion<>(
            "must cache select with where",
            new Cacheable(
                new QueryOf("SELECT id, name, address FROM person WHERE id = 1")
            ).value(),
            new IsTrue()
        ).affirm();
    }

    @Test
    void notCacheWithSum() throws Exception {
        new Assertion<>(
            "must not cache select with sum",
            new Cacheable(
                new QueryOf("SELECT SUM(salary) FROM person")
            ).value(),
            new IsNot<>(new IsTrue())
        ).affirm();
    }

    @Test
    void notCacheWithCount() throws Exception {
        new Assertion<>(
            "must not cache select with count",
            new Cacheable(
                new QueryOf("SELECT COUNT(*) FROM person")
            ).value(),
            new IsNot<>(new IsTrue())
        ).affirm();
    }

    @Test
    void notCacheWithGroupBy() throws Exception {
        new Assertion<>(
            "must not cache select with group by",
            new Cacheable(
                new QueryOf("SELECT id FROM person GROUP BY id")
            ).value(),
            new IsNot<>(new IsTrue())
        ).affirm();
    }

    @Test
    void cacheAComplexQueryWithWith() throws Exception {
        new Assertion<>(
            "must cache a complex select with with",
            new Cacheable(
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
            ).value(),
            new IsTrue()
        ).affirm();
    }

    @Test
    void cacheAComplexQueryWithoutWith() throws Exception {
        new Assertion<>(
            "must cache a complex select without with",
            new Cacheable(
                new QueryOf(
                    """
                    SELECT
                        u.id AS user_id,
                        u.username,
                        u.email,
                        u.created_at,
                        p.id AS profile_id,
                        p.display_name,
                        p.locale,
                        s.id AS session_id,
                        s.started_at,
                        s.ip_address,
                        d.id AS device_id,
                        d.type AS device_type,
                        d.os,
                        d.browser
                    FROM users u
                    JOIN profiles p
                        ON p.user_id = u.id
                    LEFT JOIN sessions s
                        ON s.user_id = u.id
                        AND s.active = TRUE
                    LEFT JOIN devices d
                        ON d.session_id = s.id
                    WHERE u.status = 'ACTIVE'
                        AND u.created_at BETWEEN TIMESTAMP '2023-01-01 00:00:00'
                        AND TIMESTAMP '2024-12-31 23:59:59'
                        AND p.locale IN ('pt_BR', 'en_US')
                        AND NOT EXISTS (
                            SELECT 1
                            FROM banned_users bu
                            WHERE bu.user_id = u.id
                        )
                        AND (
                            d.os = 'ANDROID'
                            OR (d.os = 'IOS' AND d.browser <> 'SAFARI')
                        )
                    ORDER BY u.created_at DESC, s.started_at DESC
                    OFFSET 10 ROWS
                    FETCH FIRST 25 ROWS ONLY
                    """
                )
            ).value(),
            new IsTrue()
        ).affirm();
    }

    @Test
    void cacheAnotherComplexQueryWithWith() throws Exception {
        new Assertion<>(
            "must cache another complex select with with",
            new Cacheable(
                new QueryOf(
                    """
                    WITH active_clients AS (
                        -- CTE to filter clients with specific criteria
                        SELECT id, name, email, country
                        FROM clients
                        WHERE status = 'ACTIVE'
                            AND (country = 'Brazil' OR country = 'Portugal')
                            AND email LIKE '%@gmail.com'
                    ),
                    premium_products AS (
                        -- CTE to fetch products from high-value categories
                        SELECT
                            p.id,
                            p.name AS product,
                            c.name AS category,
                            p.unit_price
                        FROM products p
                        JOIN categories c ON p.category_id = c.id
                        WHERE c.level = 'PREMIUM'
                            AND p.stock > 0
                    )
                    SELECT
                        ac.name AS client,
                        ac.email,
                        pp.product,
                        pp.category,
                        o.order_date,
                        o.quantity,
                        -- Simple row-level arithmetic operation (still cacheable)
                        (o.quantity * pp.unit_price) AS total_item_value
                    FROM orders o
                    JOIN active_clients ac ON o.client_id = ac.id
                    JOIN premium_products pp ON o.product_id = pp.id
                    WHERE o.order_date >= DATE '2025-01-01'
                        AND EXISTS (
                            -- Non-aggregated correlated subquery
                            SELECT 1
                            FROM promotions promo
                            WHERE promo.product_id = pp.id
                                AND promo.active = TRUE
                        )
                    ORDER BY o.order_date DESC
                    """
                )
            ).value(),
            new IsTrue()
        ).affirm();
    }
}
