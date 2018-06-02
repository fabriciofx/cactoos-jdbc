/**
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.fabriciofx.cactoos.jdbc;

import com.github.fabriciofx.cactoos.jdbc.adapter.ResultSetToType;
import com.github.fabriciofx.cactoos.jdbc.adapter.ResultSetToStream;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuthSession;
import com.github.fabriciofx.cactoos.jdbc.stmt.Insert;
import com.github.fabriciofx.cactoos.jdbc.stmt.Select;
import com.github.fabriciofx.cactoos.jdbc.stmt.Update;
import com.github.fabriciofx.cactoos.jdbc.value.BoolValue;
import com.github.fabriciofx.cactoos.jdbc.value.DateValue;
import com.github.fabriciofx.cactoos.jdbc.value.DecimalValue;
import com.github.fabriciofx.cactoos.jdbc.value.TextValue;
import org.junit.Test;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class SelectTest {
    @Test
    public void select() throws Exception {
        final Session session = new NoAuthSession(
            new H2Source("testdb")
        );
        new Results<>(
            session,
            new Update(
                "CREATE TABLE employee (" +
                    "id UUID DEFAULT RANDOM_UUID()," +
                    "name VARCHAR(50)," +
                    "birthday DATE," +
                    "address VARCHAR(100)," +
                    "married BOOLEAN," +
                    "salary DECIMAL(20,2)" +
                ")"
            ),
            new Insert(
                "INSERT INTO employee " +
                    "(name, birthday, address, married, salary) " +
                    "VALUES (?, ?, ?, ?, ?)",
                new TextValue("name", "John Wick"),
                new DateValue("birthday", "1980-08-16"),
                new TextValue("address", "Boulevard Street, 34"),
                new BoolValue("married", false),
                new DecimalValue("salary", "13456.00")
            ),
            new Insert(
                "INSERT INTO employee " +
                    "(name, birthday, address, married, salary) " +
                    "VALUES (?, ?, ?, ?, ?)",
                new TextValue("name", "Adam Park"),
                new DateValue("birthday", "1985-07-10"),
                new TextValue("address", "Sunset Place, 14"),
                new BoolValue("married", true),
                new DecimalValue("salary", "12345.00")
            )
        ).value();
        final DataStream xml = new ResultSetToStream(
            new Result<>(
                session,
                new Select(
                    "SELECT * FROM employee"
                )
            ),
            "employees",
            "employee"
        ).value();
        System.out.println(xml.asString());
    }

    @Test
    public void any() throws Exception {
        final Session session = new NoAuthSession(
            new H2Source("testdb")
        );
        new Results<>(
            session,
            new Update(
                "CREATE TABLE employee2 (" +
                    "id UUID DEFAULT RANDOM_UUID()," +
                    "name VARCHAR(50)," +
                    "birthday DATE," +
                    "address VARCHAR(100)," +
                    "married BOOLEAN," +
                    "salary DECIMAL(20,2)" +
                    ")"
            ),
            new Insert(
                "INSERT INTO employee2 " +
                    "(name, birthday, address, married, salary) " +
                    "VALUES (?, ?, ?, ?, ?)",
                new TextValue("name", "John Wick"),
                new DateValue("birthday", "1980-08-16"),
                new TextValue("address", "Boulevard Street, 34"),
                new BoolValue("married", false),
                new DecimalValue("salary", "13456.00")
            ),
            new Insert(
                "INSERT INTO employee2 " +
                    "(name, birthday, address, married, salary) " +
                    "VALUES (?, ?, ?, ?, ?)",
                new TextValue("name", "Adam Park"),
                new DateValue("birthday", "1985-07-10"),
                new TextValue("address", "Sunset Place, 14"),
                new BoolValue("married", true),
                new DecimalValue("salary", "12345.00")
            )
        ).value();
        final String name = new ResultSetToType<>(
            new Result<>(
                session,
                new Select(
                    "SELECT name FROM employee2"
                )
            ),
            String.class
        ).value();
        System.out.println(name);
    }
}
