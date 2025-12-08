/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018-2025 Fabrício Barros Cabral
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
package com.github.fabriciofx.cactoos.jdbc.query;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.IsText;

/**
 * Counted query tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.8.0
 * @checkstyle JavadocMethodCheck (500 lines)
 */
@SuppressWarnings(
    {
        "PMD.AvoidDuplicateLiterals",
        "PMD.TestClassWithoutTestCases",
        "PMD.ProhibitPlainJunitAssertionsRule"
    }
)
final class CountedTest {
    @Test
    void withCountForAllColumns() throws Exception {
        MatcherAssert.assertThat(
            "This query contains a count statement",
            new Counted(new QueryOf("SELECT COUNT(*) FROM employee")),
            new IsText("SELECT COUNT(*) FROM employee")
        );
    }

    @Test
    void withCountForOneColumn() throws Exception {
        MatcherAssert.assertThat(
            "This query contains a count statement",
            new Counted(new QueryOf("SELECT COUNT(id) FROM employee")),
            new IsText("SELECT COUNT(id) FROM employee")
        );
    }

    @Test
    void withoutCount() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
                new Counted(new QueryOf("SELECT * FROM employee")).asString();
            }
        );
    }
}
