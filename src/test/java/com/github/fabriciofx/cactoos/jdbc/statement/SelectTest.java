/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018-2023 Fabrício Barros Cabral
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Servers;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.param.BoolOf;
import com.github.fabriciofx.cactoos.jdbc.param.DateOf;
import com.github.fabriciofx.cactoos.jdbc.param.DecimalOf;
import com.github.fabriciofx.cactoos.jdbc.param.IntOf;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import com.github.fabriciofx.cactoos.jdbc.query.BatchOf;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.result.ResultSetAsValue;
import com.github.fabriciofx.cactoos.jdbc.result.ResultSetAsXml;
import com.github.fabriciofx.cactoos.jdbc.server.H2Server;
import com.github.fabriciofx.cactoos.jdbc.server.MysqlServer;
import com.github.fabriciofx.cactoos.jdbc.server.PgsqlServer;
import com.jcabi.matchers.XhtmlMatchers;
import java.time.LocalDate;
import org.cactoos.text.Joined;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * StatementSelect tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings(
    {
        "PMD.AvoidInstantiatingObjectsInLoops",
        "PMD.AvoidDuplicateLiterals",
        "PMD.TestClassWithoutTestCases"
    }
)
final class SelectTest {
    @Test
    void select() throws Exception {
        try (
            Servers servers = new Servers(
                new H2Server(),
                new MysqlServer(),
                new PgsqlServer()
            )
        ) {
            for (final Session session : servers.sessions()) {
                new Update(
                    session,
                    new QueryOf(
                        new Joined(
                            " ",
                            "CREATE TABLE employee (id INT,",
                            "name VARCHAR(50), birthday DATE,",
                            "address VARCHAR(100),",
                            "married BOOLEAN, salary DECIMAL(20,2),",
                            "PRIMARY KEY (id))"
                        )
                    )
                ).result();
                new Batch(
                    session,
                    new BatchOf(
                        new Joined(
                            " ",
                            "INSERT INTO employee",
                            "(id, name, birthday, address, married, salary)",
                            "VALUES (:id, :name, :birthday, :address,",
                            ":married, :salary)"
                        ),
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
                ).result();
                MatcherAssert.assertThat(
                    XhtmlMatchers.xhtml(
                        new ResultSetAsXml(
                            new Select(
                                session,
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
                        String.join(
                            "",
                            "/employees/employee/address[text()=",
                            "'Boulevard Street, 34']"
                        ),
                        "/employees/employee/married[text()='false']",
                        "/employees/employee/salary[text()='13456.00']",
                        "/employees/employee/id[text()='2']",
                        "/employees/employee/name[text()='Adam Park']",
                        String.join(
                            "",
                            "/employees/employee/address[text()=",
                            "'Sunset Place, 14']"
                        ),
                        "/employees/employee/married[text()='true']",
                        "/employees/employee/salary[text()='12345.00']"
                    )
                );
            }
        }
    }

    @Test
    void any() throws Exception {
        try (
            Servers servers = new Servers(
                new H2Server(),
                new MysqlServer(),
                new PgsqlServer()
            )
        ) {
            for (final Session session : servers.sessions()) {
                new Update(
                    session,
                    new QueryOf(
                        new Joined(
                            " ",
                            "CREATE TABLE person (id INT, name VARCHAR(30),",
                            "created_at DATE, city VARCHAR(20),",
                            "working BOOLEAN, height DECIMAL(20,2),",
                            "PRIMARY KEY (id))"
                        )
                    )
                ).result();
                new Batch(
                    session,
                    new BatchOf(
                        new Joined(
                            " ",
                            "INSERT INTO person",
                            "(id, name, created_at, city, working, height)",
                            "VALUES (:id, :name, :created_at, :city,",
                            ":working, :height)"
                        ),
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
                ).result();
                MatcherAssert.assertThat(
                    "Can't select a person name",
                    new ResultSetAsValue<>(
                        new Select(
                            session,
                            new QueryOf(
                                "SELECT name FROM person"
                            )
                        )
                    ),
                    new HasValue<>("Rob Pike")
                );
            }
        }
    }
}
