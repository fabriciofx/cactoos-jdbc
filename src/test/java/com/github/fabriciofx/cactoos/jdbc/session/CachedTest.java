/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.cache.Cache;
import com.github.fabriciofx.cactoos.cache.base.CacheOf;
import com.github.fabriciofx.cactoos.cache.key.KeyOf;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.cache.TableStore;
import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.query.NamedQuery;
import com.github.fabriciofx.cactoos.jdbc.query.Normalized;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.scalar.ResultSetAsValue;
import com.github.fabriciofx.cactoos.jdbc.source.NoAuth;
import com.github.fabriciofx.cactoos.jdbc.statement.Insert;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import com.github.fabriciofx.fake.server.RandomName;
import com.github.fabriciofx.fake.server.db.source.H2Source;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;
import org.llorllale.cactoos.matchers.IsTrue;

/**
 * {@link Cached} tests.
 *
 * @since 0.9.0
 */
final class CachedTest {

    @Test
    void dontExposeUncommittedRowToAnotherSession() throws Exception {
        final Source source = new NoAuth(
            new H2Source(new RandomName().asString())
        );
        final Cache<Query, Table> cache = new CacheOf<>(new TableStore());
        try (
            Session first = new Cached(source.session(), cache);
            Session second = new Cached(source.session(), cache)
        ) {
            new Update(
                first,
                new QueryOf("CREATE TABLE pilot (id INT, name VARCHAR(30))")
            ).execute();
            first.autocommit(false);
            new Insert(
                first,
                new QueryOf("INSERT INTO pilot (id, name) VALUES (14, 'Rook')")
            ).execute();
            new ResultSetAsValue<>(
                new Select(first, new QueryOf("SELECT COUNT(*) FROM pilot"))
            ).value();
            new Assertion<>(
                "must not expose an uncommitted row to another session",
                new ResultSetAsValue<>(
                    new Select(
                        second,
                        new QueryOf("SELECT COUNT(*) FROM pilot")
                    )
                ),
                new HasValue<>(0L)
            ).affirm();
        }
    }

    @Test
    void selectsRowCommittedAfterAnotherSessionCached() throws Exception {
        final Source source = new NoAuth(
            new H2Source(new RandomName().asString())
        );
        final Cache<Query, Table> cache = new CacheOf<>(new TableStore());
        try (
            Session first = new Cached(source.session(), cache);
            Session second = new Cached(source.session(), cache)
        ) {
            new Update(
                first,
                new QueryOf("CREATE TABLE pilot (id INT, name VARCHAR(30))")
            ).execute();
            first.autocommit(false);
            new Insert(
                first,
                new QueryOf("INSERT INTO pilot (id, name) VALUES (38, 'Bodhi')")
            ).execute();
            new ResultSetAsValue<>(
                new Select(
                    second,
                    new QueryOf("SELECT COUNT(*) FROM pilot")
                )
            ).value();
            first.commit();
            new Assertion<>(
                "must not return a stale count after another session commit",
                new ResultSetAsValue<>(
                    new Select(
                        second,
                        new QueryOf("SELECT COUNT(*) FROM pilot")
                    )
                ),
                new HasValue<>(1L)
            ).affirm();
        }
    }

    @Test
    void selectsViewRowInsertedAfterCaching() throws Exception {
        try (
            Session session = new Cached(
                new NoAuth(
                    new H2Source(new RandomName().asString())
                ).session(),
                new CacheOf<>(new TableStore())
            )
        ) {
            new Update(
                session,
                new QueryOf("CREATE TABLE pilot (id INT, name VARCHAR(30))")
            ).execute();
            new Update(
                session,
                new QueryOf("CREATE VIEW crew AS SELECT name FROM pilot")
            ).execute();
            new Insert(
                session,
                new QueryOf("INSERT INTO pilot (id, name) VALUES (52, 'Andor')")
            ).execute();
            new ResultSetAsValue<>(
                new Select(session, new QueryOf("SELECT COUNT(*) FROM crew"))
            ).value();
            new Insert(
                session,
                new QueryOf("INSERT INTO pilot (id, name) VALUES (9, 'Melshi')")
            ).execute();
            new Assertion<>(
                "must not return a stale view after changing its table",
                new ResultSetAsValue<>(
                    new Select(
                        session,
                        new QueryOf("SELECT COUNT(*) FROM crew")
                    )
                ),
                new HasValue<>(2L)
            ).affirm();
        }
    }

    @Test
    void keepsCachedSelectOfUntouchedTableAfterCommit() throws Exception {
        final Source source = new NoAuth(
            new H2Source(new RandomName().asString())
        );
        final Cache<Query, Table> cache = new CacheOf<>(new TableStore());
        try (
            Session first = new Cached(source.session(), cache);
            Session second = new Cached(source.session(), cache)
        ) {
            new Update(
                first,
                new QueryOf("CREATE TABLE pilot (id INT, name VARCHAR(30))")
            ).execute();
            new Update(
                first,
                new QueryOf("CREATE TABLE ship (id INT, model VARCHAR(30))")
            ).execute();
            new ResultSetAsValue<>(
                new Select(second, new QueryOf("SELECT COUNT(*) FROM ship"))
            ).value();
            first.autocommit(false);
            new Insert(
                first,
                new QueryOf("INSERT INTO pilot (id, name) VALUES (24, 'Kay')")
            ).execute();
            first.commit();
        }
        new Assertion<>(
            "must keep a cached select of a table the commit did not change",
            cache.store().contains(
                new KeyOf<>(
                    new Normalized(new QueryOf("SELECT COUNT(*) FROM ship"))
                )
            ),
            new IsTrue()
        ).affirm();
    }

    @Test
    void forgetsRolledBackTablesOnNextCommit() throws Exception {
        final Source source = new NoAuth(
            new H2Source(new RandomName().asString())
        );
        final Cache<Query, Table> cache = new CacheOf<>(new TableStore());
        try (
            Session first = new Cached(source.session(), cache);
            Session second = new Cached(source.session(), cache)
        ) {
            new Update(
                first,
                new QueryOf("CREATE TABLE pilot (id INT, name VARCHAR(30))")
            ).execute();
            new Update(
                first,
                new QueryOf("CREATE TABLE ship (id INT, model VARCHAR(30))")
            ).execute();
            first.autocommit(false);
            new Insert(
                first,
                new QueryOf("INSERT INTO pilot (id, name) VALUES (63, 'Zay')")
            ).execute();
            first.rollback();
            new ResultSetAsValue<>(
                new Select(second, new QueryOf("SELECT COUNT(*) FROM pilot"))
            ).value();
            new Insert(
                first,
                new QueryOf("INSERT INTO ship (id, model) VALUES (2, 'A-Wing')")
            ).execute();
            first.commit();
        }
        new Assertion<>(
            "must not invalidate tables of a rolled back transaction on commit",
            cache.store().contains(
                new KeyOf<>(
                    new Normalized(new QueryOf("SELECT COUNT(*) FROM pilot"))
                )
            ),
            new IsTrue()
        ).affirm();
    }

    @Test
    void keepsColumnOrderOfCachedSelect() throws Exception {
        try (
            Session session = new Cached(
                new NoAuth(new H2Source(new RandomName().asString())).session(),
                new CacheOf<>(new TableStore())
            )
        ) {
            new Update(
                session,
                new QueryOf("CREATE TABLE pilot (id INT, name VARCHAR(30))")
            ).execute();
            new Insert(
                session,
                new QueryOf("INSERT INTO pilot (id, name) VALUES (5, 'Biggs')")
            ).execute();
            new Assertion<>(
                "must not reorder the columns of a cached select",
                new ResultSetAsValue<>(
                    new Select(session, new QueryOf("SELECT name, id FROM pilot"))
                ),
                new HasValue<>("Biggs")
            ).affirm();
        }
    }

    @Test
    void bindsParamsInOrderOfCachedSelect() throws Exception {
        try (
            Session session = new Cached(
                new NoAuth(new H2Source(new RandomName().asString())).session(),
                new CacheOf<>(new TableStore())
            )
        ) {
            new Update(
                session,
                new QueryOf("CREATE TABLE pilot (id INT, name VARCHAR(30))")
            ).execute();
            new Insert(
                session,
                new QueryOf("INSERT INTO pilot (id, name) VALUES (4, 'Wedge')")
            ).execute();
            new Assertion<>(
                "must not bind params to reordered conditions of a select",
                new ResultSetAsValue<>(
                    new Select(
                        session,
                        new NamedQuery(
                            "SELECT name FROM pilot WHERE name = :name AND id = :id",
                            new TextParam("name", "Wedge"),
                            new IntParam("id", 4)
                        )
                    )
                ),
                new HasValue<>("Wedge")
            ).affirm();
        }
    }

    @Test
    void doesNotShareCacheBetweenDistinctColumnOrders() throws Exception {
        try (
            Session session = new Cached(
                new NoAuth(new H2Source(new RandomName().asString())).session(),
                new CacheOf<>(new TableStore())
            )
        ) {
            new Update(
                session,
                new QueryOf("CREATE TABLE pilot (id INT, name VARCHAR(30))")
            ).execute();
            new Insert(
                session,
                new QueryOf("INSERT INTO pilot (id, name) VALUES (9, 'Hobbie')")
            ).execute();
            new ResultSetAsValue<>(
                new Select(session, new QueryOf("SELECT name, id FROM pilot"))
            ).value();
            new Assertion<>(
                "must not serve a cached select with another column order",
                new ResultSetAsValue<>(
                    new Select(session, new QueryOf("SELECT id, name FROM pilot"))
                ),
                new HasValue<>(9)
            ).affirm();
        }
    }

    @Test
    void doesNotShareCacheBetweenSwappedParamConditions() throws Exception {
        try (
            Session session = new Cached(
                new NoAuth(new H2Source(new RandomName().asString())).session(),
                new CacheOf<>(new TableStore())
            )
        ) {
            new Update(
                session,
                new QueryOf("CREATE TABLE grid (x INT, y INT)")
            ).execute();
            new Insert(
                session,
                new QueryOf("INSERT INTO grid (x, y) VALUES (3, 8), (8, 3)")
            ).execute();
            new ResultSetAsValue<>(
                new Select(
                    session,
                    new NamedQuery(
                        "SELECT x FROM grid WHERE x = :p AND y = :q",
                        new IntParam("p", 3),
                        new IntParam("q", 8)
                    )
                )
            ).value();
            new Assertion<>(
                "must not serve a cached select whose params match other columns",
                new ResultSetAsValue<>(
                    new Select(
                        session,
                        new NamedQuery(
                            "SELECT x FROM grid WHERE y = :p AND x = :q",
                            new IntParam("p", 3),
                            new IntParam("q", 8)
                        )
                    )
                ),
                new HasValue<>(8)
            ).affirm();
        }
    }
}
