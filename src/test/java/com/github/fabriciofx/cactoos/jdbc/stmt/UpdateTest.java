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

import com.github.fabriciofx.cactoos.jdbc.MySqlSource;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.query.SimpleQuery;
import com.github.fabriciofx.cactoos.jdbc.result.ResultAsValue;
import com.github.fabriciofx.cactoos.jdbc.session.AuthSession;
import java.io.IOException;
import org.cactoos.text.JoinedText;
import org.hamcrest.MatcherAssert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.llorllale.cactoos.matchers.ScalarHasValue;

/**
 * Update tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 */
public final class UpdateTest {
//    private static final Server server = new MySqlServer(12345, "testdb");
//
//    @BeforeClass
//    public static void setUpAll() throws Exception {
//        UpdateTest.server.start();
//    }
//
//    @AfterClass
//    public static void tearDownAll() throws Exception {
//        UpdateTest.server.stop();
//    }

//    @Ignore
    @Test
    public void createDatabase() throws IOException {
        MatcherAssert.assertThat(
            "Can't create a database",
            new ResultAsValue<>(
                new Update(
                    new AuthSession(
                        new MySqlSource(""),
                        "root",
                        ""
                    ),
                    new SimpleQuery(
                        new JoinedText(
                            " ",
                            "CREATE DATABASE IF NOT EXISTS foodb CHARACTER ",
                            "SET utf8mb4 COLLATE utf8mb4_unicode_ci"
                        )
                    )
                ),
                Integer.class
            ),
            new ScalarHasValue<>(1)
        );
    }

    @BeforeClass
    public static void setup() throws Exception {
        new Update(
            new AuthSession(
                new MySqlSource(""),
                "root",
                ""
            ),
            new SimpleQuery(
                new JoinedText(
                    "",
                    "CREATE DATABASE IF NOT EXISTS testdb CHARACTER ",
                    "SET utf8mb4 COLLATE utf8mb4_unicode_ci"
                ).asString()
            )
        ).result();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        new Update(
            new AuthSession(
                new MySqlSource(""),
                "root",
                ""
            ),
            new SimpleQuery("DROP DATABASE IF EXISTS testdb")
        ).result();
    }

    @Test
    public void createTable() throws Exception {
         final Session[] sessions = {
//            new NoAuthSession(
//                new H2Source("testdb")
//            ),
            new AuthSession(
//                new MySqlSource("localhost", 12345, "testdb"),
                new MySqlSource("testdb"),
                "root",
                ""
            )
        };
        for (final Session session : sessions) {
            MatcherAssert.assertThat(
                "Can't create a table",
                new ResultAsValue<>(
                    new Update(
                        session,
                        new SimpleQuery(
                            new JoinedText(
                                " ",
                                "CREATE TABLE testdb.foo1",
                                "(id INT AUTO_INCREMENT,",
                                "name VARCHAR(50), PRIMARY KEY (id))"
                            )
                        )
                    ),
                    Integer.class
                ),
                new ScalarHasValue<>(0)
            );
        }
    }
}
