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

import com.github.fabriciofx.cactoos.jdbc.query.NamedQuery;
import com.github.fabriciofx.cactoos.jdbc.value.BoolValue;
import com.github.fabriciofx.cactoos.jdbc.value.DateValue;
import com.github.fabriciofx.cactoos.jdbc.value.DecimalValue;
import com.github.fabriciofx.cactoos.jdbc.value.TextValue;
import org.junit.Test;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version Id
 * @since
 */
public final class NamedQueryTest {
    @Test
    public void withoutValues() throws Exception {
        System.out.println(
            new NamedQuery(
                "SELECT * FROM employee"
            ).asString()
        );
    }

    @Test
    public void valid() throws Exception {
        System.out.println(
            new NamedQuery(
                "INSERT INTO foo2 (name) VALUES (:name)",
                new TextValue("name", "Yegor Bugayenko")
            ).asString()
        );
    }

    @Test(expected = Exception.class)
    public void invalid() throws Exception {
        System.out.println(
            new NamedQuery(
                "INSERT INTO foo2 (name) VALUES (:name)",
                new TextValue("address", "Sunset Boulevard")
            ).asString()
        );
    }

    @Test
    public void manyValues() throws Exception {
        System.out.println(
            new NamedQuery(
                "INSERT INTO employee " +
                    "(name, birthday, address, married, salary) " +
                    "VALUES (:name, :birthday, :address, :married, :salary)",
                new TextValue("name", "John Wick"),
                new DateValue("birthday", "1980-08-16"),
                new TextValue("address", "Boulevard Street, 34"),
                new BoolValue("married", false),
                new DecimalValue("salary", "13456.00")
            ).asString()
        );
    }

    @Test(expected = Exception.class)
    public void outOfOrder() throws Exception {
        System.out.println(
            new NamedQuery(
                "INSERT INTO employee " +
                    "(name, birthday, address, married, salary) " +
                    "VALUES (:name, :birthday, :address, :married, :salary)",
                new TextValue("name", "John Wick"),
                new DateValue("address", "1980-08-16"),
                new TextValue("birthday", "Boulevard Street, 34"),
                new BoolValue("married", false),
                new DecimalValue("salary", "13456.00")
            ).asString()
        );
    }
}
