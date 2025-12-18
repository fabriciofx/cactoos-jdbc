/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.adapter;

import com.github.fabriciofx.cactoos.jdbc.Server;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.script.ScriptOf;
import com.github.fabriciofx.cactoos.jdbc.server.H2Server;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.jcabi.matchers.XhtmlMatchers;
import java.sql.ResultSet;
import org.cactoos.io.ResourceOf;
import org.cactoos.text.Joined;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

/**
 * StatementSelect tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
final class ResultSetAsXmlTest {
    @Test
    void xml() throws Exception {
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
                    new QueryOf(
                        new Joined(
                            " ",
                            "SELECT name, number, carrier FROM contact",
                            "JOIN phone ON contact.id = phone.contact_id"
                        )
                    )
                ).result()
            ) {
                MatcherAssert.assertThat(
                    "Must convert a contact into a XML",
                    XhtmlMatchers.xhtml(
                        new ResultSetAsXml(
                            "contacts",
                            "contact"
                        ).adapt(rset)
                    ),
                    XhtmlMatchers.hasXPaths(
                        "/contacts/contact/name[text()='Joseph Klimber']",
                        "/contacts/contact/number[text()='(87)99991-1234']",
                        "/contacts/contact/carrier[text()='TIM']",
                        "/contacts/contact/number[text()='(87)89234-9876']",
                        "/contacts/contact/carrier[text()='Oi']"
                    )
                );
            }
        }
    }
}
