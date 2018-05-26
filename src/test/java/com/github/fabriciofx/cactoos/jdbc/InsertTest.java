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

import com.github.fabriciofx.cactoos.jdbc.adapter.RsetIntAdapter;
import com.github.fabriciofx.cactoos.jdbc.adapter.RsetUuidAdapter;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuthSession;
import com.github.fabriciofx.cactoos.jdbc.stmt.Insert;
import com.github.fabriciofx.cactoos.jdbc.stmt.InsertWithKeys;
import com.github.fabriciofx.cactoos.jdbc.stmt.Update;
import org.junit.Test;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class InsertTest {
    @Test
    public void insert() throws Exception {
        System.out.println(
            new Result<>(
                new NoAuthSession(
                    new H2Source("testdb")
                ),
                new SmartStatements(
                    new Update(
                        "CREATE TABLE foo2 (id INT AUTO_INCREMENT, name VARCHAR(50))"
                    )
                ),
                new Insert(
                    "INSERT INTO foo2 (name) VALUES (?)",
                    new TextParam("name", "Yegor Bugayenko")
                )
            ).value()
        );
    }

    @Test
    public void insertWithKeys() throws Exception {
        System.out.println(
            new Result<>(
                new NoAuthSession(
                    new H2Source("testdb")
                ),
                new SmartStatements(
                    new Update(
                        "CREATE TABLE foo3 (id INT AUTO_INCREMENT, name VARCHAR(50))"
                    )
                ),
                new InsertWithKeys<>(
                    new RsetIntAdapter(),
                    "INSERT INTO foo3 (name) VALUES (?)",
                    new TextParam("name", "Yegor Bugayenko")
                )
            ).value()
        );
    }

    @Test
    public void insertWithKeysUuid() throws Exception {
        System.out.println(
            new Result<>(
                new NoAuthSession(
                    new H2Source("testdb")
                ),
                new SmartStatements(
                    new Update(
                        "CREATE TABLE foo4 (id UUID DEFAULT RANDOM_UUID(), name VARCHAR(50))"
                    )
                ),
                new InsertWithKeys<>(
                    new RsetUuidAdapter(),
                    "INSERT INTO foo4 (name) VALUES (?)",
                    new TextParam("name", "Yegor Bugayenko")
                )
            ).value()
        );
    }
}
