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
package com.github.fabriciofx.cactoos.jdbc.connection;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.session.Logged;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuth;
import com.github.fabriciofx.cactoos.jdbc.source.H2Source;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import java.util.logging.Logger;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * StatementInsert tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
final class LoggedTest {
    @Test
    void loggedUpdate() throws Exception {
        final Logger logger = new FakeLogger();
        final String sql =
            "CREATE TABLE t012 (id INT AUTO_INCREMENT, name VARCHAR(50))";
        final Session session = new Logged(
            new NoAuth(
                new H2Source("testdb")
            ),
            "cactoos-jdbc",
            logger
        );
        new Update(session, new QueryOf(sql)).result();
        MatcherAssert.assertThat(
            "Can't connection from cactoos-jdbc",
            logger.toString(),
            Matchers.allOf(
                Matchers.containsString(
                    "Connection[#0] has been opened with properties numServers=0"
                ),
                Matchers.containsString(
                    String.format(
                        "PreparedStatement[#0] created using SQL '%s'",
                        sql
                    )
                ),
                Matchers.containsString(
                    "PreparedStatement[#0] updated a source and returned '0' in"
                ),
                Matchers.containsString("PreparedStatement[#0] closed")
            )
        );
    }
}
