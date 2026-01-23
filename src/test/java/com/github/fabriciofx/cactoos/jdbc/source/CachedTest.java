/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.source;

import com.github.fabriciofx.cactoos.cache.base.CacheOf;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.cache.TableStore;
import com.github.fabriciofx.cactoos.jdbc.param.BoolParam;
import com.github.fabriciofx.cactoos.jdbc.param.DateParam;
import com.github.fabriciofx.cactoos.jdbc.param.DecimalParam;
import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.query.NamedQuery;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.statement.Insert;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import com.github.fabriciofx.fake.logger.FakeLogger;
import com.github.fabriciofx.fake.server.RandomName;
import com.github.fabriciofx.fake.server.db.source.H2Source;
import java.sql.ResultSet;
import java.time.LocalDate;
import org.cactoos.text.TextOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasString;
import org.llorllale.cactoos.matchers.Matches;

/**
 * Cached tests.
 *
 * @since 0.9.0
 * @checkstyle MethodLengthCheck (500 lines)
 */
@SuppressWarnings({"PMD.UnitTestShouldIncludeAssert", "PMD.AvoidDuplicateLiterals"})
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
                    new DateParam("created_at", LocalDate.now()),
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
                'eea4b833fac6f4e321611e2a66529376': false\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must store a cache value",
            new HasString(
                """
                Storing in cache with key \
                'eea4b833fac6f4e321611e2a66529376' and value\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must check if cache has a value already stored",
            new HasString(
                """
                Checking if cache has a value for key \
                'eea4b833fac6f4e321611e2a66529376': true\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must retrieve a cached value",
            new HasString(
                """
                Retrieving from cache with key \
                'eea4b833fac6f4e321611e2a66529376' and value\
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
                    new DateParam("created_at", LocalDate.now()),
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
                    new DateParam("created_at", LocalDate.now()),
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
                'fa3a631bc0523744e743bf276a57a7a8': false\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must store a cache value",
            new HasString(
                """
                Storing in cache with key \
                'fa3a631bc0523744e743bf276a57a7a8' and value\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must check if cache has a value already stored",
            new HasString(
                """
                Checking if cache has a value for key \
                'fa3a631bc0523744e743bf276a57a7a8': true\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must retrieve a cached value",
            new HasString(
                """
                Retrieving from cache with key \
                'fa3a631bc0523744e743bf276a57a7a8' and value\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
        new Assertion<>(
            "must invalidate a cached value",
            new HasString(
                """
                Invalidating 1 cache entries with keys: \
                'fa3a631bc0523744e743bf276a57a7a8'\
                """
            ),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
    }
}
