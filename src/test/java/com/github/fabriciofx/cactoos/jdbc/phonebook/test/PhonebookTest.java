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
package com.github.fabriciofx.cactoos.jdbc.phonebook.test;

import com.github.fabriciofx.cactoos.jdbc.Servers;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.pagination.Pages;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phonebook;
import com.github.fabriciofx.cactoos.jdbc.phonebook.sql.SqlPhonebook;
import com.github.fabriciofx.cactoos.jdbc.script.ScriptOf;
import com.github.fabriciofx.cactoos.jdbc.server.H2Server;
import com.github.fabriciofx.cactoos.jdbc.server.PgsqlServer;
import com.jcabi.matchers.XhtmlMatchers;
import org.cactoos.io.ResourceOf;
import org.cactoos.text.Joined;
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
        "PMD.AvoidInstantiatingObjectsInLoops",
        "PMD.AvoidDuplicateLiterals",
        "PMD.TestClassWithoutTestCases"
    }
)
final class PhonebookTest {
    @Test
    void addContact() throws Exception {
        try (
            Servers servers = new Servers(
                new H2Server(
                    new ScriptOf(
                        new ResourceOf(
                            new Joined(
                                "/",
                                "com/github/fabriciofx/cactoos/jdbc/phonebook",
                                "phonebook-h2.sql"
                            )
                        )
                    )
                ),
                new PgsqlServer(
                    new ScriptOf(
                        new ResourceOf(
                            new Joined(
                                "/",
                                "com/github/fabriciofx/cactoos/jdbc/phonebook",
                                "phonebook-pgsql.sql"
                            )
                        )
                    )
                )
            )
        ) {
            for (final Session session : servers.sessions()) {
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
            Servers servers = new Servers(
                new H2Server(
                    new ScriptOf(
                        new ResourceOf(
                            new Joined(
                                "/",
                                "com/github/fabriciofx/cactoos/jdbc/phonebook",
                                "phonebook-h2.sql"
                            )
                        )
                    )
                ),
                new PgsqlServer(
                    new ScriptOf(
                        new ResourceOf(
                            new Joined(
                                "/",
                                "com/github/fabriciofx/cactoos/jdbc/phonebook",
                                "phonebook-pgsql.sql"
                            )
                        )
                    )
                )
            )
        ) {
            for (final Session session : servers.sessions()) {
                MatcherAssert.assertThat(
                    "Must have contact name",
                    XhtmlMatchers.xhtml(
                        new SqlPhonebook(session)
                            .search("maria")
                            .page(0)
                            .content()
                            .get(0)
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
            Servers servers = new Servers(
                new H2Server(
                    new ScriptOf(
                        new ResourceOf(
                            new Joined(
                                "/",
                                "com/github/fabriciofx/cactoos/jdbc/phonebook",
                                "phonebook-h2.sql"
                            )
                        )
                    )
                ),
                new PgsqlServer(
                    new ScriptOf(
                        new ResourceOf(
                            new Joined(
                                "/",
                                "com/github/fabriciofx/cactoos/jdbc/phonebook",
                                "phonebook-pgsql.sql"
                            )
                        )
                    )
                )
            )
        ) {
            for (final Session session : servers.sessions()) {
                final Phonebook phonebook = new SqlPhonebook(session);
                final Contact contact = phonebook.search("maria")
                    .page(0)
                    .content()
                    .get(0);
                contact.update("Maria Lima");
                MatcherAssert.assertThat(
                    "Must update contact name",
                    XhtmlMatchers.xhtml(
                        new SqlPhonebook(session)
                            .search("maria")
                            .page(0)
                            .content()
                            .get(0)
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
            Servers servers = new Servers(
                new H2Server(
                    new ScriptOf(
                        new ResourceOf(
                            new Joined(
                                "/",
                                "com/github/fabriciofx/cactoos/jdbc/phonebook",
                                "phonebook-h2.sql"
                            )
                        )
                    )
                ),
                new PgsqlServer(
                    new ScriptOf(
                        new ResourceOf(
                            new Joined(
                                "/",
                                "com/github/fabriciofx/cactoos/jdbc/phonebook",
                                "phonebook-pgsql.sql"
                            )
                        )
                    )
                )
            )
        ) {
            for (final Session session : servers.sessions()) {
                final Pages<Contact> pages = new SqlPhonebook(session)
                    .contacts(1);
                new Assertion<>(
                    "Must match all phonebook's contacts",
                    XhtmlMatchers.xhtml(
                        pages.page(0).content().get(0).about()
                    ),
                    XhtmlMatchers.hasXPaths(
                        "/contact/name[text()='Joseph Klimber']"
                    )
                ).affirm();
                new Assertion<>(
                    "Must match all phonebook's contacts",
                    XhtmlMatchers.xhtml(
                        pages.page(1).content().get(0).about()
                    ),
                    XhtmlMatchers.hasXPaths(
                        "/contact/name[text()='Maria Souza']"
                    )
                ).affirm();
                new Assertion<>(
                    "Must match all phonebook's contacts",
                    XhtmlMatchers.xhtml(
                        pages.page(2).content().get(0).about()
                    ),
                    XhtmlMatchers.hasXPaths(
                        "/contact/name[text()='Jeff Duham']"
                    )
                ).affirm();
            }
        }
    }

}
