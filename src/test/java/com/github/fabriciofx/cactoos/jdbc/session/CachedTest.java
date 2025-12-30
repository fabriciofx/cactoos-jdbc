package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.cache.Logged;
import com.github.fabriciofx.cactoos.jdbc.cache.ResultSetCache;
import com.github.fabriciofx.cactoos.jdbc.param.BoolOf;
import com.github.fabriciofx.cactoos.jdbc.param.DateOf;
import com.github.fabriciofx.cactoos.jdbc.param.DecimalOf;
import com.github.fabriciofx.cactoos.jdbc.param.IntOf;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.statement.Insert;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import com.github.fabriciofx.fake.logger.FakeLogger;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;
import org.llorllale.cactoos.matchers.IsTrue;

final class CachedTest {
    @Test
    void cacheASelect() throws Exception {
        final String name = "Rob Pike";
        final String city = "San Francisco";
        try (Server<DataSource> server = new H2Server()) {
            server.start();
            final FakeLogger logger = new FakeLogger();
            final Session session = new Cached(
                new NoAuth(server.resource()),
                new Logged<>(new ResultSetCache(), logger)
            );
            try (Connection connection = session.connection()) {
                new Update(
                    connection,
                    new QueryOf(
                        "CREATE TABLE person (id INT, name VARCHAR(30), created_at DATE, city VARCHAR(20), working BOOLEAN, height DECIMAL(20,2), PRIMARY KEY (id))"
                    )
                ).execute();
                new Insert(
                    connection,
                    new QueryOf(
                        "INSERT INTO person (id, name, created_at, city, working, height) VALUES (:id, :name, :created_at, :city, :working, :height)",
                        new IntOf("id", 1),
                        new TextOf("name", name),
                        new DateOf("created_at", LocalDate.now()),
                        new TextOf("city", city),
                        new BoolOf("working", true),
                        new DecimalOf("height", "1.86")
                    )
                ).execute();
                try (
                    ResultSet rset = new Select(
                        connection,
                        new QueryOf("SELECT name FROM person")
                    ).execute()
                ) {
                    new Assertion<>(
                        "must have the name",
                        rset.next(),
                        new IsTrue()
                    ).affirm();
                    new Assertion<>(
                        "must have the correct name",
                        new org.cactoos.text.TextOf(rset.getString("name")),
                        new IsText(name)
                    ).affirm();
                }
                try (
                    ResultSet rset = new Select(
                        connection,
                        new QueryOf("SELECT city FROM person")
                    ).execute()
                ) {
                    new Assertion<>(
                        "must have the city",
                        rset.next(),
                        new IsTrue()
                    ).affirm();
                    new Assertion<>(
                        "must have the correct city",
                        new org.cactoos.text.TextOf(rset.getString("city")),
                        new IsText(city)
                    ).affirm();
                }
            }
            System.out.println(logger.toString());
        }
    }
}
