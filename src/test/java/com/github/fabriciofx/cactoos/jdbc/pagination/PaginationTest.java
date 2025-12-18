/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
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
@SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
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
                    new Assertion<>(
                        "Must retrieve the contact's id",
                        new TextOf(rset.getObject(1).toString()),
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
