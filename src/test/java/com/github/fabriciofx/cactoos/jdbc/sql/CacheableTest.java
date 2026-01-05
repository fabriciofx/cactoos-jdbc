package com.github.fabriciofx.cactoos.jdbc.sql;

import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsTrue;

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
                        ac.id          AS customer_id,
                        ac.name        AS customer_name,
                        ac.country,
                        ac.segment,
                        ro.id          AS order_id,
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
                        u.id            AS user_id,
                        u.username,
                        u.email,
                        u.created_at,
                        p.id            AS profile_id,
                        p.display_name,
                        p.locale,
                        s.id            AS session_id,
                        s.started_at,
                        s.ip_address,
                        d.id            AS device_id,
                        d.type          AS device_type,
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
                    WITH clientes_ativos AS (
                        -- CTE para filtrar clientes com critérios específicos
                        SELECT id, nome, email, pais
                        FROM clientes
                        WHERE status = 'ATIVO'\s
                          AND (pais = 'Brasil' OR pais = 'Portugal')
                          AND email LIKE '%@gmail.com'
                    ),
                    produtos_premium AS (
                        -- CTE para buscar produtos de categorias de alto valor
                        SELECT p.id, p.nome AS produto, c.nome AS categoria, p.preco_unitario
                        FROM produtos p
                        JOIN categorias c ON p.categoria_id = c.id
                        WHERE c.nivel = 'PREMIUM' AND p.estoque > 0
                    )
                    SELECT\s
                        ca.nome AS cliente,
                        ca.email,
                        pp.produto,
                        pp.categoria,
                        ped.data_pedido,
                        ped.quantidade,
                        -- Operação matemática simples de linha (ainda cacheável)
                        (ped.quantidade * pp.preco_unitario) AS valor_total_item
                    FROM pedidos ped
                    JOIN clientes_ativos ca ON ped.cliente_id = ca.id
                    JOIN produtos_premium pp ON ped.produto_id = pp.id
                    WHERE ped.data_pedido >= '2025-01-01'
                      AND EXISTS (
                          -- Subquery correlacionada não agregada
                          SELECT 1\s
                          FROM promocoes promo\s
                          WHERE promo.produto_id = pp.id\s
                            AND promo.ativa = true
                      )
                    ORDER BY ped.data_pedido DESC
                    """
                )
            ).value(),
            new IsTrue()
        ).affirm();
    }
}
