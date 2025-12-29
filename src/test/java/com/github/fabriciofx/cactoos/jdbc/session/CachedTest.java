package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Session;
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

final class CachedTest {
    @Test
    void cacheASelect() throws Exception {
        try (Server<DataSource> server = new H2Server()) {
            server.start();
            final FakeLogger logger = new FakeLogger();
            final Session session = new Logged(
                    new NoAuth(server.resource()),
                "cached",
                logger
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
                        new TextOf("name", "Rob Pike"),
                        new DateOf("created_at", LocalDate.now()),
                        new TextOf("city", "San Francisco"),
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
                    if (rset.next()) {
                        final String name = rset.getString("name");
                        System.out.println(name);
                    }
                }
                try (
                    ResultSet rset = new Select(
                        connection,
                        new QueryOf("SELECT city FROM person")
                    ).execute()
                ) {
                    if (rset.next()) {
                        final String city = rset.getString("city");
                        System.out.println(city);
                    }
                }
            }
            System.out.println(logger.toString());
        }
    }
}
