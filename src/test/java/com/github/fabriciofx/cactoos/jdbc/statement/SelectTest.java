/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.param.BoolOf;
import com.github.fabriciofx.cactoos.jdbc.param.DateOf;
import com.github.fabriciofx.cactoos.jdbc.param.DecimalOf;
import com.github.fabriciofx.cactoos.jdbc.param.IntOf;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import com.github.fabriciofx.cactoos.jdbc.query.BatchedQuery;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.result.ResultSetAsValue;
import com.github.fabriciofx.cactoos.jdbc.result.ResultSetAsXml;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuth;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import com.jcabi.matchers.XhtmlMatchers;
import java.sql.Connection;
import java.time.LocalDate;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * Select tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class SelectTest {
    @Test
    void select() throws Exception {
        try (Server<DataSource> server = new H2Server()) {
            try (Connection connection = new NoAuth(server.resource()).connection()) {
                new Update(
                    connection,
                    new QueryOf(
                        "CREATE TABLE employee (id INT, name VARCHAR(50), birthday DATE, address VARCHAR(100), married BOOLEAN, salary DECIMAL(20,2), PRIMARY KEY (id))"
                    )
                ).execute();
                new Batch(
                    connection,
                    new BatchedQuery(
                        "INSERT INTO employee (id, name, birthday, address, married, salary) VALUES (:id, :name, :birthday, :address, :married, :salary)",
                        new ParamsOf(
                            new IntOf("id", 1),
                            new TextOf("name", "John Wick"),
                            new DateOf("birthday", "1980-08-15"),
                            new TextOf("address", "Boulevard Street, 34"),
                            new BoolOf("married", false),
                            new DecimalOf("salary", "13456.00")
                        ),
                        new ParamsOf(
                            new IntOf("id", 2),
                            new TextOf("name", "Adam Park"),
                            new DateOf("birthday", "1985-07-09"),
                            new TextOf("address", "Sunset Place, 14"),
                            new BoolOf("married", true),
                            new DecimalOf("salary", "12345.00")
                        )
                    )
                ).execute();
                new Assertion<>(
                    "must insert employees records",
                    XhtmlMatchers.xhtml(
                        new ResultSetAsXml(
                            new Select(
                                connection,
                                new QueryOf(
                                    "SELECT * FROM employee"
                                )
                            ),
                            "employees",
                            "employee"
                        ).value()
                    ),
                    XhtmlMatchers.hasXPaths(
                        "/employees/employee/id[text()='1']",
                        "/employees/employee/name[text()='John Wick']",
                        "/employees/employee/address[text()='Boulevard Street, 34']",
                        "/employees/employee/married[text()='false']",
                        "/employees/employee/salary[text()='13456.00']",
                        "/employees/employee/id[text()='2']",
                        "/employees/employee/name[text()='Adam Park']",
                        "/employees/employee/address[text()='Sunset Place, 14']",
                        "/employees/employee/married[text()='true']",
                        "/employees/employee/salary[text()='12345.00']"
                    )
                ).affirm();
            }
        }
    }

    @Test
    void any() throws Exception {
        try (Server<DataSource> server = new H2Server()) {
            server.start();
            try (
                Connection connection = new NoAuth(server.resource()).connection()
            ) {
                new Update(
                    connection,
                    new QueryOf(
                        "CREATE TABLE person (id INT, name VARCHAR(30), created_at DATE, city VARCHAR(20), working BOOLEAN, height DECIMAL(20,2), PRIMARY KEY (id))"
                    )
                ).execute();
                new Batch(
                    connection,
                    new BatchedQuery(
                        "INSERT INTO person (id, name, created_at, city, working, height) VALUES (:id, :name, :created_at, :city, :working, :height)",
                        new ParamsOf(
                            new IntOf("id", 1),
                            new TextOf("name", "Rob Pike"),
                            new DateOf("created_at", LocalDate.now()),
                            new TextOf("city", "San Francisco"),
                            new BoolOf("working", true),
                            new DecimalOf("height", "1.86")
                        ),
                        new ParamsOf(
                            new IntOf("id", 2),
                            new TextOf("name", "Ana Pivot"),
                            new DateOf("created_at", LocalDate.now()),
                            new TextOf("city", "Washington"),
                            new BoolOf("working", false),
                            new DecimalOf("height", "1.62")
                        )
                    )
                ).execute();
                new Assertion<>(
                    "must select a person name",
                    new ResultSetAsValue<>(
                        new Select(
                            connection,
                            new QueryOf(
                                "SELECT name FROM person"
                            )
                        )
                    ),
                    new HasValue<>("Rob Pike")
                ).affirm();
            }
        }
    }
}
