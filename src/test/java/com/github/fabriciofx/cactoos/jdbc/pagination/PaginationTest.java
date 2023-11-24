/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018-2023 Fabrício Barros Cabral
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
package com.github.fabriciofx.cactoos.jdbc.pagination;

import com.github.fabriciofx.cactoos.jdbc.Server;
import com.github.fabriciofx.cactoos.jdbc.phonebook.sql.SqlPhonebook;
import com.github.fabriciofx.cactoos.jdbc.query.Paginated;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.script.ScriptOf;
import com.github.fabriciofx.cactoos.jdbc.server.H2Server;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import java.sql.ResultSet;
import java.util.UUID;
import org.cactoos.io.ResourceOf;
import org.cactoos.text.Joined;
import org.cactoos.text.TextOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsNumber;
import org.llorllale.cactoos.matchers.IsText;

/**
 * Pagination tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.8.0
 * @checkstyle JavadocMethodCheck (500 lines)
 */
final class PaginationTest {
    @Test
    void paginated() throws Exception {
        try (
            Server server = new H2Server(
                new ScriptOf(
                    new ResourceOf(
                        new Joined(
                            "/",
                            "com/github/fabriciofx/cactoos/jdbc/phonebook",
                            "phonebook-h2.sql"
                        )
                    )
                )
            )
        ) {
            server.start();
            try (
                ResultSet rset = new Select(
                    server.session(),
                    new Paginated(new QueryOf("SELECT * FROM contact"), 1, 0)
                ).result()
            ) {
                if (rset.next()) {
                    final UUID id = (UUID) rset.getObject(1);
                    new Assertion<>(
                        "Must retrieve the contact's id",
                        new TextOf(id.toString()),
                        new IsText("2d1ebc5b-7d27-4197-9cf0-e84451c5bbb1")
                    ).affirm();
                }
            }
        }
    }

    @Test
    void count() throws Exception {
        try (
            Server server = new H2Server(
                new ScriptOf(
                    new ResourceOf(
                        new Joined(
                            "/",
                            "com/github/fabriciofx/cactoos/jdbc/phonebook",
                            "phonebook-h2.sql"
                        )
                    )
                )
            )
        ) {
            server.start();
            new Assertion<>(
                "Must have at least one page",
                new SqlPhonebook(server.session()).search("maria").count(),
                new IsNumber(1)
            ).affirm();
        }
    }
}
