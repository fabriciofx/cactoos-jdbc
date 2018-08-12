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
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.query.param.BoolParam;
import com.github.fabriciofx.cactoos.jdbc.query.param.DateParam;
import com.github.fabriciofx.cactoos.jdbc.query.param.DecimalParam;
import com.github.fabriciofx.cactoos.jdbc.query.param.TextParam;
import org.cactoos.text.JoinedText;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.llorllale.cactoos.matchers.TextHasString;

/**
 * SimpleQuery tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class SimpleQueryTest {
    @Test
    public void withoutValues() throws Exception {
        MatcherAssert.assertThat(
            "Can't build a named query without values",
            new SimpleQuery("SELECT * FROM employee"),
            new TextHasString("SELECT * FROM employee")
        );
    }

    @Test
    public void valid() throws Exception {
        MatcherAssert.assertThat(
            "Can't build a simple named query",
            new SimpleQuery(
                "INSERT INTO foo2 (name) VALUES (:name)",
                new TextParam("name", "Yegor Bugayenko")
            ),
            new TextHasString("INSERT INTO foo2 (name) VALUES (?)")
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalid() throws Exception {
        new SimpleQuery(
            "INSERT INTO foo2 (name) VALUES (:name)",
            new TextParam("address", "Sunset Boulevard")
        ).asString();
    }

    @Test
    public void manyValues() throws Exception {
        MatcherAssert.assertThat(
            "Can't build a named query with many values",
            new SimpleQuery(
                new JoinedText(
                    " ",
                    "INSERT INTO employee",
                    "(name, birthday, address, married, salary)",
                    "VALUES (:name, :birthday, :address, :married, :salary)"
                ),
                new TextParam("name", "John Wick"),
                new DateParam("birthday", "1980-08-16"),
                new TextParam("address", "Boulevard Street, 34"),
                new BoolParam("married", false),
                new DecimalParam("salary", "13456.00")
            ),
            new TextHasString(
                new JoinedText(
                    " ",
                    "INSERT INTO employee",
                    "(name, birthday, address, married, salary)",
                    "VALUES (?, ?, ?, ?, ?)"
                )
            )
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void outOfOrder() throws Exception {
        new SimpleQuery(
            new JoinedText(
                " ",
                "INSERT INTO employee",
                "(name, birthday, address, married, salary)",
                "VALUES (:name, :birthday, :address, :married, :salary)"
            ),
            new TextParam("name", "John Wick"),
            new DateParam("address", "1980-08-16"),
            new TextParam("birthday", "Boulevard Street, 34"),
            new BoolParam("married", false),
            new DecimalParam("salary", "13456.00")
        ).asString();
    }
}
