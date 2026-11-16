/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.source;

import com.github.fabriciofx.cactoos.cache.Cache;
import com.github.fabriciofx.cactoos.cache.base.CacheOf;
import com.github.fabriciofx.cactoos.cache.key.KeyOf;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.cache.TableStore;
import com.github.fabriciofx.cactoos.jdbc.param.BoolParam;
import com.github.fabriciofx.cactoos.jdbc.param.DateParam;
import com.github.fabriciofx.cactoos.jdbc.param.DecimalParam;
import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import com.github.fabriciofx.cactoos.jdbc.query.NamedQuery;
import com.github.fabriciofx.cactoos.jdbc.query.Normalized;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.scalar.ResultSetAsValue;
import com.github.fabriciofx.cactoos.jdbc.statement.Batch;
import com.github.fabriciofx.cactoos.jdbc.statement.Insert;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import com.github.fabriciofx.fake.logger.FakeLogger;
import com.github.fabriciofx.fake.server.RandomName;
import com.github.fabriciofx.fake.server.db.source.H2Source;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.ZoneId;
import org.cactoos.text.TextOf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasString;
import org.llorllale.cactoos.matchers.HasValue;
import org.llorllale.cactoos.matchers.IsTrue;
import org.llorllale.cactoos.matchers.Matches;

/**
 * Cached tests.
 *
 * @since 0.9.0
 * @checkstyle FullyQualifiedTypeCheck (500 lines)
 * @checkstyle ClassFanOutComplexityCheck (1000 lines)
 */
final class CachedTest {

    @Test
    void cacheASelect() throws Exception {
        final FakeLogger logger = new FakeLogger();
        final Source source = new Logged(
            new Cached(
                new NoAuth(new H2Source(new RandomName().asString())),
                new com.github.fabriciofx.cactoos.cache.base.Logged<>(
                    new CacheOf<>(new TableStore()),
                    "cache",
                    logger
                )
            ),
            "cache",
            logger
        );
        try (Session session = source.session()) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE person (id INT, name VARCHAR(30),
                    created_at DATE, city VARCHAR(20), working
                    BOOLEAN, height DECIMAL(20,2), PRIMARY KEY (id))
                    """
                )
            ).execute();
            new Insert(
                session,
                new NamedQuery(
                    """
                    INSERT INTO person (id, name, created_at, city,
                    working, height) VALUES (:id, :name, :created_at,
                    :city, :working, :height)
                    """,
                    new IntParam("id", 1),
                    new TextParam("name", "Rob Pike"),
                    new DateParam(
                        "created_at",
                        LocalDate.now(ZoneId.systemDefault())
                    ),
                    new TextParam("city", "San Francisco"),
                    new BoolParam("working", true),
                    new DecimalParam("height", "1.86")
                )
            ).execute();
            try (
                ResultSet rset = new Select(
                    session,
                    new NamedQuery(
                        "SELECT id, name FROM person WHERE id = :id",
                        new IntParam("id", 1)
                    )
                ).execute()
            ) {
                if (rset.next()) {
                    rset.getString("name");
                }
            }
            try (
                ResultSet rset = new Select(
                    session,
                    new NamedQuery(
                        "SELECT name, id FROM person WHERE id = :id",
                        new IntParam("id", 1)
                    )
                ).execute()
            ) {
                if (rset.next()) {
                    rset.getString("name");
                }
            }
        }
        new Assertion<>(
            "must check if cache has a value",
            new HasString(
                """
                Checking if cache has a value for key \
                '423915ac363acf6f': false\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must store a cache value",
            new HasString(
                """
                Storing in cache with key '423915ac363acf6f' and value\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must check if cache has a value already stored",
            new HasString(
                """
                Checking if cache has a value for key \
                '423915ac363acf6f': true\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must retrieve a cached value",
            new HasString(
                """
                Retrieving from cache with key '423915ac363acf6f' and value\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
    }

    @Test
    void deleteACachedResult() throws Exception {
        final FakeLogger logger = new FakeLogger();
        final Source source = new Logged(
            new Cached(
                new NoAuth(new H2Source(new RandomName().asString())),
                new com.github.fabriciofx.cactoos.cache.base.Logged<>(
                    new CacheOf<>(new TableStore()),
                    "cache",
                    logger
                )
            ),
            "cache",
            logger
        );
        try (Session session = source.session()) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE person (id INT, name VARCHAR(30),
                    created_at DATE, city VARCHAR(20), working
                    BOOLEAN, height DECIMAL(20,2), PRIMARY KEY (id))
                    """
                )
            ).execute();
            new Insert(
                session,
                new NamedQuery(
                    """
                    INSERT INTO person (id, name, created_at, city,
                    working, height) VALUES (:id, :name, :created_at,
                    :city, :working, :height)
                    """,
                    new IntParam("id", 1),
                    new TextParam("name", "Rob Pike"),
                    new DateParam(
                        "created_at",
                        LocalDate.now(ZoneId.systemDefault())
                    ),
                    new TextParam("city", "San Francisco"),
                    new BoolParam("working", true),
                    new DecimalParam("height", "1.86")
                )
            ).execute();
            new Insert(
                session,
                new NamedQuery(
                    """
                    INSERT INTO person (id, name, created_at, city,
                    working, height) VALUES (:id, :name, :created_at,
                    :city, :working, :height)
                    """,
                    new IntParam("id", 2),
                    new TextParam("name", "Maria Souza"),
                    new DateParam(
                        "created_at",
                        LocalDate.now(ZoneId.systemDefault())
                    ),
                    new TextParam("city", "New York"),
                    new BoolParam("working", false),
                    new DecimalParam("height", "1.62")
                )
            ).execute();
            try (
                ResultSet rset = new Select(
                    session,
                    new NamedQuery(
                        "SELECT name FROM person WHERE id = :id",
                        new IntParam("id", 1)
                    )
                ).execute()
            ) {
                if (rset.next()) {
                    rset.getString("name");
                }
            }
            try (
                ResultSet rset = new Select(
                    session,
                    new NamedQuery(
                        "SELECT name FROM person WHERE id = :id",
                        new IntParam("id", 1)
                    )
                ).execute()
            ) {
                if (rset.next()) {
                    rset.getString("name");
                }
            }
            new Update(
                session,
                new NamedQuery(
                    "DELETE FROM person WHERE id = :id",
                    new IntParam("id", 1)
                )
            ).execute();
        }
        new Assertion<>(
            "must check if cache has a value",
            new HasString(
                """
                Checking if cache has a value for key \
                '4fdf480b89704977': false\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must store a cache value",
            new HasString(
                """
                Storing in cache with key '4fdf480b89704977' and value\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must check if cache has a value already stored",
            new HasString(
                """
                Checking if cache has a value for key \
                '4fdf480b89704977': true\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must retrieve a cached value",
            new HasString(
                """
                Retrieving from cache with key '4fdf480b89704977' and value\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must invalidate a cached value",
            new HasString(
                """
                Invalidating 1 cache entries with keys: \
                '4fdf480b89704977'\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
    }

    @Test
    void retrievesCachedValueOnRepeatedSelect() throws Exception {
        try (
            Session session = new Cached(
                new NoAuth(new H2Source(new RandomName().asString())),
                new CacheOf<>(new TableStore())
            ).session()
        ) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE pilot (id INT, name VARCHAR(30),
                    PRIMARY KEY (id))
                    """
                )
            ).execute();
            new Insert(
                session,
                new QueryOf(
                    "INSERT INTO pilot (id, name) VALUES (7, 'Jyn Erso')"
                )
            ).execute();
            new ResultSetAsValue<>(
                new Select(session, new QueryOf("SELECT name FROM pilot"))
            ).value();
            new Assertion<>(
                "must retrieve the same value from cache on a repeated select",
                new ResultSetAsValue<>(
                    new Select(session, new QueryOf("SELECT name FROM pilot"))
                ),
                new HasValue<>("Jyn Erso")
            ).affirm();
        }
    }

    @Test
    void selectsRowInsertedAfterCaching() throws Exception {
        try (
            Session session = new Cached(
                new NoAuth(new H2Source(new RandomName().asString())),
                new CacheOf<>(new TableStore())
            ).session()
        ) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE pilot (id INT, name VARCHAR(30),
                    PRIMARY KEY (id))
                    """
                )
            ).execute();
            new Insert(
                session,
                new QueryOf("INSERT INTO pilot (id, name) VALUES (13, 'Wedge')")
            ).execute();
            new ResultSetAsValue<>(
                new Select(session, new QueryOf("SELECT COUNT(*) FROM pilot"))
            ).value();
            new Insert(
                session,
                new NamedQuery(
                    "INSERT INTO pilot (id, name) VALUES (:id, :name)",
                    new IntParam("id", 41),
                    new TextParam("name", "Biggs")
                )
            ).execute();
            new Assertion<>(
                "must not return a stale count after an insert",
                new ResultSetAsValue<>(
                    new Select(
                        session,
                        new QueryOf("SELECT COUNT(*) FROM pilot")
                    )
                ),
                new HasValue<>(2L)
            ).affirm();
        }
    }

    @Test
    void selectsRowUpdatedAfterCaching() throws Exception {
        try (
            Session session = new Cached(
                new NoAuth(new H2Source(new RandomName().asString())),
                new CacheOf<>(new TableStore())
            ).session()
        ) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE pilot (id INT, name VARCHAR(30),
                    PRIMARY KEY (id))
                    """
                )
            ).execute();
            new Insert(
                session,
                new QueryOf("INSERT INTO pilot (id, name) VALUES (5, 'Poe')")
            ).execute();
            new ResultSetAsValue<>(
                new Select(
                    session,
                    new NamedQuery(
                        "SELECT name FROM pilot WHERE id = :id",
                        new IntParam("id", 5)
                    )
                )
            ).value();
            new Update(
                session,
                new NamedQuery(
                    "UPDATE pilot SET name = :name WHERE id = :id",
                    new TextParam("name", "Poe Dameron"),
                    new IntParam("id", 5)
                )
            ).execute();
            new Assertion<>(
                "must not return a stale name after an update",
                new ResultSetAsValue<>(
                    new Select(
                        session,
                        new NamedQuery(
                            "SELECT name FROM pilot WHERE id = :id",
                            new IntParam("id", 5)
                        )
                    )
                ),
                new HasValue<>("Poe Dameron")
            ).affirm();
        }
    }

    @Test
    void dontSelectRowDeletedAfterCaching() throws Exception {
        try (
            Session session = new Cached(
                new NoAuth(new H2Source(new RandomName().asString())),
                new CacheOf<>(new TableStore())
            ).session()
        ) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE pilot (id INT, name VARCHAR(30),
                    PRIMARY KEY (id))
                    """
                )
            ).execute();
            new Insert(
                session,
                new QueryOf(
                    "INSERT INTO pilot (id, name) VALUES (89, 'Hobbie')"
                )
            ).execute();
            new ResultSetAsValue<>(
                new Select(session, new QueryOf("SELECT COUNT(*) FROM pilot"))
            ).value();
            new Update(
                session,
                new QueryOf("DELETE FROM pilot WHERE id = 89")
            ).execute();
            new Assertion<>(
                "must not return a deleted row from cache",
                new ResultSetAsValue<>(
                    new Select(
                        session,
                        new QueryOf("SELECT COUNT(*) FROM pilot")
                    )
                ),
                new HasValue<>(0L)
            ).affirm();
        }
    }

    @Test
    void selectsDistinctValuesForDistinctParams() throws Exception {
        try (
            Session session = new Cached(
                new NoAuth(new H2Source(new RandomName().asString())),
                new CacheOf<>(new TableStore())
            ).session()
        ) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE pilot (id INT, name VARCHAR(30),
                    PRIMARY KEY (id))
                    """
                )
            ).execute();
            new Batch(
                session,
                new NamedQuery(
                    "INSERT INTO pilot (id, name) VALUES (:id, :name)",
                    new ParamsOf(
                        new IntParam("id", 3),
                        new TextParam("name", "Tallie")
                    ),
                    new ParamsOf(
                        new IntParam("id", 17),
                        new TextParam("name", "Jek Porkins")
                    )
                )
            ).execute();
            new ResultSetAsValue<>(
                new Select(
                    session,
                    new NamedQuery(
                        "SELECT name FROM pilot WHERE id = :id",
                        new IntParam("id", 3)
                    )
                )
            ).value();
            new Assertion<>(
                "must not mix cached values of queries with distinct params",
                new ResultSetAsValue<>(
                    new Select(
                        session,
                        new NamedQuery(
                            "SELECT name FROM pilot WHERE id = :id",
                            new IntParam("id", 17)
                        )
                    )
                ),
                new HasValue<>("Jek Porkins")
            ).affirm();
        }
    }

    @Test
    void selectsRowsInsertedByBatchAfterCaching() throws Exception {
        try (
            Session session = new Cached(
                new NoAuth(new H2Source(new RandomName().asString())),
                new CacheOf<>(new TableStore())
            ).session()
        ) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE pilot (id INT, name VARCHAR(30),
                    PRIMARY KEY (id))
                    """
                )
            ).execute();
            new ResultSetAsValue<>(
                new Select(session, new QueryOf("SELECT COUNT(*) FROM pilot"))
            ).value();
            new Batch(
                session,
                new NamedQuery(
                    "INSERT INTO pilot (id, name) VALUES (:id, :name)",
                    new ParamsOf(
                        new IntParam("id", 21),
                        new TextParam("name", "Dutch")
                    ),
                    new ParamsOf(
                        new IntParam("id", 34),
                        new TextParam("name", "Red Leader")
                    ),
                    new ParamsOf(
                        new IntParam("id", 55),
                        new TextParam("name", "Gold Five")
                    )
                )
            ).execute();
            new Assertion<>(
                "must not return a stale count after a batch insert",
                new ResultSetAsValue<>(
                    new Select(
                        session,
                        new QueryOf("SELECT COUNT(*) FROM pilot")
                    )
                ),
                new HasValue<>(3L)
            ).affirm();
        }
    }

    @Test
    void selectsRowMergedAfterCaching() throws Exception {
        try (
            Session session = new Cached(
                new NoAuth(new H2Source(new RandomName().asString())),
                new CacheOf<>(new TableStore())
            ).session()
        ) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE pilot (id INT, name VARCHAR(30),
                    PRIMARY KEY (id))
                    """
                )
            ).execute();
            new Insert(
                session,
                new QueryOf("INSERT INTO pilot (id, name) VALUES (8, 'Lando')")
            ).execute();
            new ResultSetAsValue<>(
                new Select(session, new QueryOf("SELECT name FROM pilot"))
            ).value();
            new Update(
                session,
                new QueryOf(
                    """
                    MERGE INTO pilot p USING (SELECT 8 AS id) s ON p.id = s.id
                    WHEN MATCHED THEN UPDATE SET name = 'Lando Calrissian'
                    """
                )
            ).execute();
            new Assertion<>(
                "must not return a stale name after a merge",
                new ResultSetAsValue<>(
                    new Select(session, new QueryOf("SELECT name FROM pilot"))
                ),
                new HasValue<>("Lando Calrissian")
            ).affirm();
        }
    }

    @Test
    void dontSelectRowsTruncatedAfterCaching() throws Exception {
        try (
            Session session = new Cached(
                new NoAuth(new H2Source(new RandomName().asString())),
                new CacheOf<>(new TableStore())
            ).session()
        ) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE pilot (id INT, name VARCHAR(30),
                    PRIMARY KEY (id))
                    """
                )
            ).execute();
            new Insert(
                session,
                new QueryOf(
                    "INSERT INTO pilot (id, name) VALUES (61, 'Ten Numb')"
                )
            ).execute();
            new ResultSetAsValue<>(
                new Select(session, new QueryOf("SELECT COUNT(*) FROM pilot"))
            ).value();
            new Update(session, new QueryOf("TRUNCATE TABLE pilot")).execute();
            new Assertion<>(
                "must not return truncated rows from cache",
                new ResultSetAsValue<>(
                    new Select(
                        session,
                        new QueryOf("SELECT COUNT(*) FROM pilot")
                    )
                ),
                new HasValue<>(0L)
            ).affirm();
        }
    }

    @Test
    void selectsJoinUpdatedAfterCaching() throws Exception {
        try (
            Session session = new Cached(
                new NoAuth(new H2Source(new RandomName().asString())),
                new CacheOf<>(new TableStore())
            ).session()
        ) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE squad (id INT, title VARCHAR(30),
                    PRIMARY KEY (id))
                    """
                )
            ).execute();
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE pilot (id INT, name VARCHAR(30), squad INT,
                    PRIMARY KEY (id))
                    """
                )
            ).execute();
            new Insert(
                session,
                new QueryOf("INSERT INTO squad (id, title) VALUES (2, 'Rogue')")
            ).execute();
            new Insert(
                session,
                new QueryOf(
                    "INSERT INTO pilot (id, name, squad) VALUES (9, 'Luke', 2)"
                )
            ).execute();
            new ResultSetAsValue<>(
                new Select(
                    session,
                    new QueryOf(
                        """
                        SELECT s.title FROM pilot p JOIN squad s
                        ON p.squad = s.id
                        """
                    )
                )
            ).value();
            new Update(
                session,
                new QueryOf("UPDATE squad SET title = 'Wraith' WHERE id = 2")
            ).execute();
            new Assertion<>(
                "must not return a stale join after updating a joined table",
                new ResultSetAsValue<>(
                    new Select(
                        session,
                        new QueryOf(
                            """
                            SELECT s.title FROM pilot p JOIN squad s
                            ON p.squad = s.id
                            """
                        )
                    )
                ),
                new HasValue<>("Wraith")
            ).affirm();
        }
    }

    @Test
    void keepsCachedValueAfterChangingAnotherTable() throws Exception {
        final Cache<Query, Table> cache = new CacheOf<>(new TableStore());
        try (
            Session session = new Cached(
                new NoAuth(new H2Source(new RandomName().asString())),
                cache
            ).session()
        ) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE pilot (id INT, name VARCHAR(30),
                    PRIMARY KEY (id))
                    """
                )
            ).execute();
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE ship (id INT, model VARCHAR(30),
                    PRIMARY KEY (id))
                    """
                )
            ).execute();
            new Insert(
                session,
                new QueryOf("INSERT INTO pilot (id, name) VALUES (4, 'Arvel')")
            ).execute();
            new ResultSetAsValue<>(
                new Select(session, new QueryOf("SELECT name FROM pilot"))
            ).value();
            new Insert(
                session,
                new QueryOf("INSERT INTO ship (id, model) VALUES (6, 'Y-Wing')")
            ).execute();
        }
        new Assertion<>(
            "must not invalidate a cached select of an unchanged table",
            cache.store().contains(
                new KeyOf<>(
                    new Normalized(new QueryOf("SELECT name FROM pilot"))
                )
            ),
            new IsTrue()
        ).affirm();
    }

    @Test
    void selectsRowInsertedWithUppercaseTableName() throws Exception {
        try (
            Session session = new Cached(
                new NoAuth(new H2Source(new RandomName().asString())),
                new CacheOf<>(new TableStore())
            ).session()
        ) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE pilot (id INT, name VARCHAR(30),
                    PRIMARY KEY (id))
                    """
                )
            ).execute();
            new ResultSetAsValue<>(
                new Select(session, new QueryOf("select count(*) from pilot"))
            ).value();
            new Insert(
                session,
                new QueryOf("INSERT INTO PILOT (ID, NAME) VALUES (27, 'Kes')")
            ).execute();
            new Assertion<>(
                "must invalidate cache regardless of table name case",
                new ResultSetAsValue<>(
                    new Select(
                        session,
                        new QueryOf("select count(*) from pilot")
                    )
                ),
                new HasValue<>(1L)
            ).affirm();
        }
    }

    @Test
    void selectsRowInsertedByAnotherSession() throws Exception {
        final Source source = new Cached(
            new NoAuth(new H2Source(new RandomName().asString())),
            new CacheOf<>(new TableStore())
        );
        try (Session first = source.session()) {
            new Update(
                first,
                new QueryOf(
                    """
                    CREATE TABLE pilot (id INT, name VARCHAR(30),
                    PRIMARY KEY (id))
                    """
                )
            ).execute();
            new ResultSetAsValue<>(
                new Select(first, new QueryOf("SELECT COUNT(*) FROM pilot"))
            ).value();
            try (Session second = source.session()) {
                new Insert(
                    second,
                    new QueryOf(
                        "INSERT INTO pilot (id, name) VALUES (72, 'Zev')"
                    )
                ).execute();
            }
            new Assertion<>(
                "must not return a stale count after another session insert",
                new ResultSetAsValue<>(
                    new Select(first, new QueryOf("SELECT COUNT(*) FROM pilot"))
                ),
                new HasValue<>(1L)
            ).affirm();
        }
    }

    @Test
    void dontSelectRowRolledBackAfterCaching() throws Exception {
        try (
            Session session = new Cached(
                new NoAuth(new H2Source(new RandomName().asString())),
                new CacheOf<>(new TableStore())
            ).session()
        ) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE pilot (id INT, name VARCHAR(30),
                    PRIMARY KEY (id))
                    """
                )
            ).execute();
            session.autocommit(false);
            new Insert(
                session,
                new QueryOf(
                    "INSERT INTO pilot (id, name) VALUES (93, 'Garven')"
                )
            ).execute();
            new ResultSetAsValue<>(
                new Select(session, new QueryOf("SELECT COUNT(*) FROM pilot"))
            ).value();
            session.rollback();
            new Assertion<>(
                "must not return a rolled back row from cache",
                new ResultSetAsValue<>(
                    new Select(
                        session,
                        new QueryOf("SELECT COUNT(*) FROM pilot")
                    )
                ),
                new HasValue<>(0L)
            ).affirm();
        }
    }

    @Test
    @Timeout(10)
    void selectsWithDefaultCache() throws Exception {
        try (
            Session session = new Cached(
                new NoAuth(new H2Source(new RandomName().asString()))
            ).session()
        ) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE pilot (id INT, name VARCHAR(30),
                    PRIMARY KEY (id))
                    """
                )
            ).execute();
            new Insert(
                session,
                new QueryOf("INSERT INTO pilot (id, name) VALUES (31, 'Snap')")
            ).execute();
            new Assertion<>(
                "must select using the default cache without hanging",
                new ResultSetAsValue<>(
                    new Select(session, new QueryOf("SELECT name FROM pilot"))
                ),
                new HasValue<>("Snap")
            ).affirm();
        }
    }
}
