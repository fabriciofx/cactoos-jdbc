/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.pagination.Page;
import com.github.fabriciofx.cactoos.jdbc.phonebook.sql.SqlPhonebook;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuth;
import com.github.fabriciofx.fake.server.Server;
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
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @since 0.1
 */
@SuppressWarnings(
    {
        "PMD.AvoidDuplicateLiterals",
        "PMD.UnitTestShouldIncludeAssert"
    }
)
final class PhonebookTest {
    @Test
    void mustCreateAContact() throws Exception {
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
                final Contact contact = phonebook.create("Donald Knuth");
                contact.phones().add("99991234", "TIM");
                contact.phones().add("98812564", "Oi");
                MatcherAssert.assertThat(
                    "must contacts contain correct phone number",
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
    void mustSearchAContact() throws Exception {
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
                    "must have contact name",
                    XhtmlMatchers.xhtml(
                        new SqlPhonebook(session)
                            .search("maria")
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
    void mustRenameAContact() throws Exception {
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
                final Contact contact = phonebook.search("maria").getFirst();
                contact.update("Maria Lima");
                MatcherAssert.assertThat(
                    "Must update contact name",
                    XhtmlMatchers.xhtml(
                        new SqlPhonebook(session)
                            .search("maria")
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
    void mustDoPagination() throws Exception {
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
            final Session session = new NoAuth(server.resource());
            Page<Contact> page = new SqlPhonebook(session).page(1, 2);
            new Assertion<>(
                "must match Maria Souza phonebook contact",
                XhtmlMatchers.xhtml(page.items().getFirst().about()),
                XhtmlMatchers.hasXPaths(
                    "/contact/name[text()='Maria Souza']"
                )
            ).affirm();
            new Assertion<>(
                "must match Joseph Klimber phonebook contact",
                XhtmlMatchers.xhtml(page.items().getLast().about()),
                XhtmlMatchers.hasXPaths(
                    "/contact/name[text()='Joseph Klimber']"
                )
            ).affirm();
            page = new SqlPhonebook(session).page(2, 2);
            new Assertion<>(
                "must match Jeff Duham phonebook contact",
                XhtmlMatchers.xhtml(page.items().getFirst().about()),
                XhtmlMatchers.hasXPaths(
                    "/contact/name[text()='Jeff Duham']"
                )
            ).affirm();
        }
    }
}
