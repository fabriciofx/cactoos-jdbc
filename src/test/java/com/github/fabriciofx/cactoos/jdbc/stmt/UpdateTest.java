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

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Servers;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.query.SimpleQuery;
import com.github.fabriciofx.cactoos.jdbc.result.ResultAsValue;
import com.github.fabriciofx.cactoos.jdbc.session.AuthSession;
import com.github.fabriciofx.cactoos.jdbc.source.MySqlSource;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.cactoos.text.JoinedText;
import org.hamcrest.MatcherAssert;
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
    @Test
    public void createTable() throws Exception {
        try (final Servers servers = new Servers()) {
            for (final Session session : servers.sessions()) {
                MatcherAssert.assertThat(
                    "Can't create a table",
                    new ResultAsValue<>(
                        new Update(
                            session,
                            new SimpleQuery(
                                new JoinedText(
                                    " ",
                                    "CREATE TABLE foo1 (id INT AUTO_INCREMENT,",
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

    @Test
    public void longQuery() throws Exception {
        final Session session = new AuthSession(
            new MySqlSource("localhost", 3306, ""),
            "root",
            ""
        );
        final URL url = this.getClass().getClassLoader().getResource(
            "com/github/fabriciofx/cactoos/jdbc/agenda/agendadb-mysql-old.sql"
        );
        final List<String> lines = Files.readAllLines(
            Paths.get(url.toURI())
        );
        final String sql = String.join("", lines);
        final Query query = new SimpleQuery(sql);
        System.out.println(sql);
        System.out.println(query.asString());
        new Update(
            session,
            query
        ).result();
    }
}
