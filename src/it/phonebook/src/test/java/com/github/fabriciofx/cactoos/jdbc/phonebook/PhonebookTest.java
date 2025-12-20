/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.pagination.Pages;
import com.github.fabriciofx.cactoos.jdbc.phonebook.sql.SqlPhonebook;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuth;
import com.github.fabriciofx.fake.server.Servers;
import com.github.fabriciofx.fake.server.db.script.SqlScript;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import com.github.fabriciofx.fake.server.db.server.PgsqlServer;
import com.jcabi.matchers.XhtmlMatchers;
import javax.sql.DataSource;
import org.cactoos.io.ResourceOf;
import org.hamcrest.MatcherAssert;
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
@SuppressWarnings(
    {
        "PMD.AvoidDuplicateLiterals",
        "PMD.UnitTestShouldIncludeAssert"
    }
)
final class PhonebookTest {
    @Test
    void addContact() throws Exception {
        try (
            Servers<DataSource> servers = new Servers<>(
                new H2Server(
                    new SqlScript(
                        new ResourceOf(
                            "phonebook/phonebook-h2.sql"
                        )
                    )
                ),
                new PgsqlServer(
                    new SqlScript(
                        new ResourceOf(
                            "phonebook/phonebook-pgsql.sql"
                        )
                    )
                )
            )
        ) {
            for (final DataSource source : servers.resources()) {
                final Session session = new NoAuth(source);
                final Phonebook phonebook = new SqlPhonebook(session);
                final Contact contact = phonebook.contact("Donald Knuth");
                contact.phones().add("99991234", "TIM");
                contact.phones().add("98812564", "Oi");
                MatcherAssert.assertThat(
                    "Must contacts should contain correct phone number",
                    XhtmlMatchers.xhtml(
                        contact.about()
                    ),
                    XhtmlMatchers.hasXPaths(
                        "/contact/name[text()='Donald Knuth']",
                        "/contact/phones/phone/number[text()='99991234']",
                        "/contact/phones/phone/carrier[text()='TIM']",
                        "/contact/phones/phone/number[text()='98812564']",
                        "/contact/phones/phone/carrier[text()='Oi']"
                    )
                );
            }
        }
    }

    @Test
    void findContact() throws Exception {
        try (
            Servers<DataSource> servers = new Servers<>(
                new H2Server(
                    new SqlScript(
                        new ResourceOf(
                            "phonebook/phonebook-h2.sql"
                        )
                    )
                ),
                new PgsqlServer(
                    new SqlScript(
                        new ResourceOf(
                            "phonebook/phonebook-pgsql.sql"
                        )
                    )
                )
            )
        ) {
            for (final DataSource source : servers.resources()) {
                final Session session = new NoAuth(source);
                MatcherAssert.assertThat(
                    "Must have contact name",
                    XhtmlMatchers.xhtml(
                        new SqlPhonebook(session)
                            .search("maria")
                            .page(0)
                            .content()
                            .getFirst()
                            .about()
                    ),
                    XhtmlMatchers.hasXPaths(
                        "/contact/name[text()='Maria Souza']"
                    )
                );
            }
        }
    }

    @Test
    void renameContact() throws Exception {
        try (
            Servers<DataSource> servers = new Servers<>(
                new H2Server(
                    new SqlScript(
                        new ResourceOf(
                            "phonebook/phonebook-h2.sql"
                        )
                    )
                ),
                new PgsqlServer(
                    new SqlScript(
                        new ResourceOf(
                            "phonebook/phonebook-pgsql.sql"
                        )
                    )
                )
            )
        ) {
            for (final DataSource source : servers.resources()) {
                final Session session = new NoAuth(source);
                final Phonebook phonebook = new SqlPhonebook(session);
                final Contact contact = phonebook.search("maria")
                    .page(0)
                    .content()
                    .getFirst();
                contact.update("Maria Lima");
                MatcherAssert.assertThat(
                    "Must update contact name",
                    XhtmlMatchers.xhtml(
                        new SqlPhonebook(session)
                            .search("maria")
                            .page(0)
                            .content()
                            .getFirst()
                            .about()
                    ),
                    XhtmlMatchers.hasXPaths(
                        "/contact/name[text()='Maria Lima']"
                    )
                );
            }
        }
    }

    @Test
    void contacts() throws Exception {
        try (
            Servers<DataSource> servers = new Servers<>(
                new H2Server(
                    new SqlScript(
                        new ResourceOf(
                            "phonebook/phonebook-h2.sql"
                        )
                    )
                ),
                new PgsqlServer(
                    new SqlScript(
                        new ResourceOf(
                            "phonebook/phonebook-pgsql.sql"
                        )
                    )
                )
            )
        ) {
            for (final DataSource source : servers.resources()) {
                final Session session = new NoAuth(source);
                final Pages<Contact> pages = new SqlPhonebook(session)
                    .contacts(1);
                new Assertion<>(
                    "Must match all phonebook's contacts",
                    XhtmlMatchers.xhtml(
                        pages.page(0).content().getFirst().about()
                    ),
                    XhtmlMatchers.hasXPaths(
                        "/contact/name[text()='Joseph Klimber']"
                    )
                ).affirm();
                new Assertion<>(
                    "Must match all phonebook's contacts",
                    XhtmlMatchers.xhtml(
                        pages.page(1).content().getFirst().about()
                    ),
                    XhtmlMatchers.hasXPaths(
                        "/contact/name[text()='Maria Souza']"
                    )
                ).affirm();
                new Assertion<>(
                    "Must match all phonebook's contacts",
                    XhtmlMatchers.xhtml(
                        pages.page(2).content().getFirst().about()
                    ),
                    XhtmlMatchers.hasXPaths(
                        "/contact/name[text()='Jeff Duham']"
                    )
                ).affirm();
            }
        }
    }
}
