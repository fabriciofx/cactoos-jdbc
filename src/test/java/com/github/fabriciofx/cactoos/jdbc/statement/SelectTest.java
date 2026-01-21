/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.param.BoolParam;
import com.github.fabriciofx.cactoos.jdbc.param.DateParam;
import com.github.fabriciofx.cactoos.jdbc.param.DecimalParam;
import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import com.github.fabriciofx.cactoos.jdbc.query.NamedQuery;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.scalar.ResultSetAsValue;
import com.github.fabriciofx.cactoos.jdbc.scalar.ResultSetAsXml;
import com.github.fabriciofx.cactoos.jdbc.source.NoAuth;
import com.github.fabriciofx.fake.server.RandomName;
import com.github.fabriciofx.fake.server.db.source.H2Source;
import com.jcabi.matchers.XhtmlMatchers;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * Select tests.
 *
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
final class SelectTest {
    @Test
    void select() throws Exception {
        try (
            Session session = new NoAuth(
                new H2Source(
                    new RandomName().asString()
                )
            ).session()
        ) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE employee (id INT, name VARCHAR(50), birthday
                    DATE, address VARCHAR(100), married BOOLEAN, salary
                    DECIMAL(20,2), PRIMARY KEY (id))
                    """
                )
            ).execute();
            new Batch(
                session,
                new NamedQuery(
                    """
                    INSERT INTO employee (id, name, birthday, address, married,
                    salary) VALUES (:id, :name, :birthday, :address, :married,
                    :salary)
                    """,
                    new ParamsOf(
                        new IntParam("id", 1),
                        new TextParam("name", "John Wick"),
                        new DateParam("birthday", "1980-08-15"),
                        new TextParam(
                            "address",
                            "Boulevard Street, 34"
                        ),
                        new BoolParam("married", false),
                        new DecimalParam("salary", "13456.00")
                    ),
                    new ParamsOf(
                        new IntParam("id", 2),
                        new TextParam("name", "Adam Park"),
                        new DateParam("birthday", "1985-07-09"),
                        new TextParam("address", "Sunset Place, 14"),
                        new BoolParam("married", true),
                        new DecimalParam("salary", "12345.00")
                    )
                )
            ).execute();
            new Assertion<>(
                "must insert employees records",
                XhtmlMatchers.xhtml(
                    new ResultSetAsXml(
                        new Select(
                            session,
                            new QueryOf("SELECT * FROM employee")
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

    @Test
    void any() throws Exception {
        try (
            Session session = new NoAuth(
                new H2Source(
                    new RandomName().asString()
                )
            ).session()
        ) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE person (id INT, name VARCHAR(30), created_at
                    DATE, city VARCHAR(20), working BOOLEAN, height
                    DECIMAL(20,2), PRIMARY KEY (id))
                    """
                )
            ).execute();
            new Batch(
                session,
                new NamedQuery(
                    """
                    INSERT INTO person (id, name, created_at, city, working,
                    height) VALUES (:id, :name, :created_at, :city, :working,
                    :height)
                    """,
                    new ParamsOf(
                        new IntParam("id", 1),
                        new TextParam("name", "Rob Pike"),
                        new DateParam("created_at", LocalDate.now()),
                        new TextParam("city", "San Francisco"),
                        new BoolParam("working", true),
                        new DecimalParam("height", "1.86")
                    ),
                    new ParamsOf(
                        new IntParam("id", 2),
                        new TextParam("name", "Ana Pivot"),
                        new DateParam("created_at", LocalDate.now()),
                        new TextParam("city", "Washington"),
                        new BoolParam("working", false),
                        new DecimalParam("height", "1.62")
                    )
                )
            ).execute();
            new Assertion<>(
                "must select a person name",
                new ResultSetAsValue<>(
                    new Select(
                        session,
                        new QueryOf("SELECT name FROM person")
                    )
                ),
                new HasValue<>("Rob Pike")
            ).affirm();
        }
    }
}
