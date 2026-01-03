/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.scalar;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.scalar.ResultSetAsXml;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuth;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.script.SqlScript;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import com.jcabi.matchers.XhtmlMatchers;
import javax.sql.DataSource;
import org.cactoos.io.ResourceOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;

/**
 * ResultSetAsXml tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @checkstyle NestedTryDepthCheck (500 lines)
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
final class ResultSetAsXmlTest {
    @Test
    void xml() throws Exception {
        try (
            Server<DataSource> server = new H2Server(
                new SqlScript(
                    new ResourceOf(
                        "phonebook/phonebook-h2.sql"
                    )
                )
            )
        ) {
            server.start();
            try (Connexio connexio = new NoAuth(server.resource()).connexio()) {
                new Assertion<>(
                    "must convert a contact into a XML",
                    XhtmlMatchers.xhtml(
                        new ResultSetAsXml(
                            new Select(
                                connexio,
                                new QueryOf(
                                    "SELECT name, number, carrier FROM contact JOIN phone ON contact.id = phone.contact_id"
                                )
                            ),
                            "contacts",
                            "contact"
                        ).value()
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
