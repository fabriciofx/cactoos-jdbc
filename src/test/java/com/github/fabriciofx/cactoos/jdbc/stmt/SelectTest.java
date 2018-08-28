/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018 Fabrício Barros Cabral
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.fabriciofx.cactoos.jdbc.stmt;

import com.github.fabriciofx.cactoos.jdbc.Servers;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.SmartQueryParams;
import com.github.fabriciofx.cactoos.jdbc.query.BatchQuery;
import com.github.fabriciofx.cactoos.jdbc.query.SimpleQuery;
import com.github.fabriciofx.cactoos.jdbc.query.param.BoolParam;
import com.github.fabriciofx.cactoos.jdbc.query.param.DateParam;
import com.github.fabriciofx.cactoos.jdbc.query.param.DecimalParam;
import com.github.fabriciofx.cactoos.jdbc.query.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.query.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.result.ResultAsValues;
import com.github.fabriciofx.cactoos.jdbc.result.ResultAsXml;
import com.github.fabriciofx.cactoos.jdbc.server.H2Server;
import com.github.fabriciofx.cactoos.jdbc.server.MysqlServer;
import com.github.fabriciofx.cactoos.jdbc.server.PsqlServer;
import com.jcabi.matchers.XhtmlMatchers;
import java.time.LocalDate;
import java.util.List;
import org.cactoos.Scalar;
import org.cactoos.text.JoinedText;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Select tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class SelectTest {
    @Test
    public void select() throws Exception {
        try (
            final Servers servers = new Servers(
                new H2Server(),
                new MysqlServer(),
                new PsqlServer()
            )
        ) {
            for (final Session session : servers.sessions()) {
                new Update(
                    session,
                    new SimpleQuery(
                        new JoinedText(
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
                    new BatchQuery(
                        new JoinedText(
                            " ",
                            "INSERT INTO employee",
                            "(id, name, birthday, address, married, salary)",
                            "VALUES (:id, :name, :birthday, :address,",
                            ":married, :salary)"
                        ),
                        new SmartQueryParams(
                            new IntParam("id", 1),
                            new TextParam("name", "John Wick" ),
                            new DateParam("birthday", "1980-08-15"),
                            new TextParam("address", "Boulevard Street, 34"),
                            new BoolParam("married", false),
                            new DecimalParam("salary", "13456.00")
                        ),
                        new SmartQueryParams(
                            new IntParam("id", 2),
                            new TextParam("name", "Adam Park"),
                            new DateParam("birthday", "1985-07-09"),
                            new TextParam("address", "Sunset Place, 14"),
                            new BoolParam("married", true),
                            new DecimalParam("salary", "12345.00")
                        )
                    )
                ).result();
                MatcherAssert.assertThat(
                    XhtmlMatchers.xhtml(
                        new ResultAsXml(
                            new Select(
                                session,
                                new SimpleQuery(
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
//                        "/employees/employee/birthday[text()='1980-08-15']",
                        "/employees/employee/address[text()='Boulevard Street, 34']",
                        "/employees/employee/married[text()='false']",
                        "/employees/employee/salary[text()='13456.00']",
                        "/employees/employee/id[text()='2']",
                        "/employees/employee/name[text()='Adam Park']",
//                        "/employees/employee/birthday[text()='1985-07-09']",
                        "/employees/employee/address[text()='Sunset Place, 14']",
                        "/employees/employee/married[text()='true']",
                        "/employees/employee/salary[text()='12345.00']"
                    )
                );
            }
        }
    }

    @Test
    public void any() throws Exception {
        try (
            final Servers servers = new Servers(
                new H2Server(),
                new MysqlServer(),
                new PsqlServer()
            )
        ) {
            for (final Session session : servers.sessions()) {
                new Update(
                    session,
                    new SimpleQuery(
                        new JoinedText(
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
                    new BatchQuery(
                        new JoinedText(
                            " ",
                            "INSERT INTO person",
                            "(id, name, created_at, city, working, height)",
                            "VALUES (:id, :name, :created_at, :city,",
                            ":working, :height)"
                        ),
                        new SmartQueryParams(
                            new IntParam("id", 1),
                            new TextParam("name", "Rob Pike"),
                            new DateParam("created_at", LocalDate.now()),
                            new TextParam("city", "San Francisco"),
                            new BoolParam("working", true),
                            new DecimalParam("height", "1.86")
                        ),
                        new SmartQueryParams(
                            new IntParam("id", 2),
                            new TextParam("name", "Ana Pivot"),
                            new DateParam("created_at", LocalDate.now()),
                            new TextParam("city", "Washington"),
                            new BoolParam("working", false),
                            new DecimalParam("height", "1.62")
                        )
                    )
                ).result();
                final Scalar<List<String>> names = new ResultAsValues<>(
                    new Select(
                        session,
                        new SimpleQuery(
                            "SELECT name FROM person"
                        )
                    )
                );
                MatcherAssert.assertThat(
                    "Can't select a person name",
                    names.value().get(0),
                    Matchers.equalTo("Rob Pike")
                );
            }
        }
    }
}
