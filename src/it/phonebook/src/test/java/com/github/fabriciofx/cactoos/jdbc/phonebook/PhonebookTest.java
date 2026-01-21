/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook;

import com.github.fabriciofx.cactoos.jdbc.Page;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.phonebook.sql.SqlPhonebook;
import com.github.fabriciofx.cactoos.jdbc.source.NoAuth;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.script.SqlScript;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import com.jcabi.matchers.XhtmlMatchers;
import javax.sql.DataSource;
import org.cactoos.io.ResourceOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;

/**
 * Phonebook tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
final class PhonebookTest {
    @Test
    void mustCreateAContact() throws Exception {
        try (
            Server<DataSource> server = new H2Server(
                new SqlScript(
                    new ResourceOf("phonebook/phonebook-h2.sql")
                )
            )
        ) {
            server.start();
            final Phonebook phonebook = new SqlPhonebook(
                new NoAuth(server.resource())
            );
            final Contact contact = phonebook.create("Donald Knuth");
            contact.phones().add("99991234", "TIM");
            contact.phones().add("98812564", "Oi");
            new Assertion<>(
                "must contacts contain correct phone number",
                XhtmlMatchers.xhtml(contact.about()),
                XhtmlMatchers.hasXPaths(
                    "/contact/name[text()='Donald Knuth']",
                    "/contact/phones/phone/number[text()='99991234']",
                    "/contact/phones/phone/carrier[text()='TIM']",
                    "/contact/phones/phone/number[text()='98812564']",
                    "/contact/phones/phone/carrier[text()='Oi']"
                )
            ).affirm();
        }
    }

    @Test
    void mustSearchAContact() throws Exception {
        try (
            Server<DataSource> server = new H2Server(
                new SqlScript(
                    new ResourceOf("phonebook/phonebook-h2.sql")
                )
            )
        ) {
            server.start();
            new Assertion<>(
                "must have contact name",
                XhtmlMatchers.xhtml(
                    new SqlPhonebook(new NoAuth(server.resource()))
                        .search("maria")
                        .get(0)
                        .about()
                ),
                XhtmlMatchers.hasXPaths("/contact/name[text()='Maria Souza']")
            ).affirm();
        }
    }

    @Test
    void mustRenameAContact() throws Exception {
        try (
            Server<DataSource> server = new H2Server(
                new SqlScript(
                    new ResourceOf("phonebook/phonebook-h2.sql")
                )
            )
        ) {
            server.start();
            final Source source = new NoAuth(server.resource());
            final Phonebook phonebook = new SqlPhonebook(source);
            final Contact contact = phonebook.search("maria").get(0);
            contact.update("Maria Lima");
            new Assertion<>(
                "must update contact name",
                XhtmlMatchers.xhtml(
                    new SqlPhonebook(source)
                        .search("maria")
                        .get(0)
                        .about()
                ),
                XhtmlMatchers.hasXPaths(
                    "/contact/name[text()='Maria Lima']"
                )
            ).affirm();
        }
    }

    @Test
    void mustDoPagination() throws Exception {
        try (
            Server<DataSource> server = new H2Server(
                new SqlScript(
                    new ResourceOf("phonebook/phonebook-h2.sql")
                )
            )
        ) {
            server.start();
            final Source source = new NoAuth(server.resource());
            Page<Contact> page = new SqlPhonebook(source).page(1, 2);
            new Assertion<>(
                "must match Maria Souza phonebook contact",
                XhtmlMatchers.xhtml(page.items().get(0).about()),
                XhtmlMatchers.hasXPaths(
                    "/contact/name[text()='Maria Souza']"
                )
            ).affirm();
            new Assertion<>(
                "must match Joseph Klimber phonebook contact",
                XhtmlMatchers.xhtml(
                    page.items().get(page.items().size() - 1).about()
                ),
                XhtmlMatchers.hasXPaths(
                    "/contact/name[text()='Joseph Klimber']"
                )
            ).affirm();
            page = new SqlPhonebook(source).page(2, 2);
            new Assertion<>(
                "must match Jeff Duham phonebook contact",
                XhtmlMatchers.xhtml(page.items().get(0).about()),
                XhtmlMatchers.hasXPaths("/contact/name[text()='Jeff Duham']")
            ).affirm();
        }
    }
}
