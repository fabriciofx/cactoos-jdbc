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

import com.github.fabriciofx.cactoos.jdbc.transformer.ResultSetAsXml;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuthSession;
import com.github.fabriciofx.cactoos.jdbc.stmt.Insert;
import com.github.fabriciofx.cactoos.jdbc.stmt.Select;
import com.github.fabriciofx.cactoos.jdbc.stmt.Transaction;
import com.github.fabriciofx.cactoos.jdbc.stmt.Update;
import com.github.fabriciofx.cactoos.jdbc.value.TextValue;
import org.junit.Test;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class TransactionTest {
    @Test
    public void transaction() throws Exception {
        System.out.println(
            new Result<DataStream>(
                new NoAuthSession(
                    new H2Source("testdb")
                ),
                new Update(
                    "CREATE TABLE foo5 (id INT AUTO_INCREMENT, name VARCHAR(50))"
                ),
                new Transaction(
                    new Insert(
                        "INSERT INTO foo5 (name) VALUES (?)",
                        new TextValue("name", "Jeff Lebowski")
                    ),
                    new Insert(
                        "INSERT INTO bar (name) VALUES (?)",
                        new TextValue("name", "Yegor Bugayenko")
                    )
                ),
                new Insert(
                    "INSERT INTO foo5 (name) VALUES (?)",
                    new TextValue("name", "Bart Simpson")
                ),
                new Select(
                    new ResultSetAsXml("employees", "employee"),
                    "SELECT * from foo5"
                )
            ).value().asString()
        );
    }
}
