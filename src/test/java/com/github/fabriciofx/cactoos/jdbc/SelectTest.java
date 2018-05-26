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

import com.github.fabriciofx.cactoos.jdbc.adapter.RsetDataStreamAdapter;
import com.github.fabriciofx.cactoos.jdbc.param.BoolParam;
import com.github.fabriciofx.cactoos.jdbc.param.DateParam;
import com.github.fabriciofx.cactoos.jdbc.param.DecimalParam;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuthSession;
import com.github.fabriciofx.cactoos.jdbc.stmt.Insert;
import com.github.fabriciofx.cactoos.jdbc.stmt.Select;
import com.github.fabriciofx.cactoos.jdbc.stmt.Update;
import org.junit.Test;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class SelectTest {
    @Test
    public void select() throws Exception {
        System.out.println(
            new Result<>(
                new NoAuthSession(
                    new H2Source("testdb")
                ),
                new SmartStatements(
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
                        new TextParam("name", "John Wick"),
                        new DateParam("birthday", "1980-08-16"),
                        new TextParam("address", "Boulevard Street, 34"),
                        new BoolParam("married", false),
                        new DecimalParam("salary", "13456.00")
                    ),
                    new Insert(
                        "INSERT INTO employee " +
                            "(name, birthday, address, married, salary) " +
                            "VALUES (?, ?, ?, ?, ?)",
                        new TextParam("name", "Adam Park"),
                        new DateParam("birthday", "1985-07-10"),
                        new TextParam("address", "Sunset Place, 14"),
                        new BoolParam("married", true),
                        new DecimalParam("salary", "12345.00")
                    )
                ),
                new Select<>(
                    new RsetDataStreamAdapter(),
                    "SELECT * FROM employee"
                )
            ).value().asString()
        );
    }
}
