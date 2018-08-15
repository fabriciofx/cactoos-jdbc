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
package com.github.fabriciofx.cactoos.jdbc.log;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.query.SimpleQuery;
import com.github.fabriciofx.cactoos.jdbc.session.LoggedSession;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuthSession;
import com.github.fabriciofx.cactoos.jdbc.source.H2Source;
import com.github.fabriciofx.cactoos.jdbc.stmt.Update;
import java.util.logging.Logger;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Insert tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 */
public final class LoggedSessionTest {
    @Test
    public void loggedUpdate() throws Exception {
        final Logger logger = new FakeLogger();
        final String sql =
            "CREATE TABLE t012 (id INT AUTO_INCREMENT, name VARCHAR(50))";
        final Session session = new LoggedSession(
            new NoAuthSession(
                new H2Source("testdb")
            ),
            "cactoos-jdbc",
            logger
        );
        new Update(session, new SimpleQuery(sql)).result();
        MatcherAssert.assertThat(
            "Can't log from cactoos-jdbc",
            logger.toString(),
            Matchers.allOf(
                Matchers.containsString(
                    "Connection[#0] has been opened with properties"
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
                Matchers.containsString("PreparedStatement[#0] closed"),
                Matchers.containsString("Connection[#0] closed")
            )
        );
    }
}
