/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018-2019 Fabricio Barros Cabral
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
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phonebook;
import com.github.fabriciofx.cactoos.jdbc.phonebook.sql.PhonebookSql;
import com.github.fabriciofx.cactoos.jdbc.script.ScriptSql;
import com.github.fabriciofx.cactoos.jdbc.server.ServerH2;
import com.github.fabriciofx.cactoos.jdbc.server.ServerPgsql;
import com.jcabi.matchers.XhtmlMatchers;
import org.cactoos.io.ResourceOf;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.cactoos.text.Joined;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

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
        "PMD.AvoidDuplicateLiterals"
    }
)
public final class PhonebookTest {
    @Test
    public void addContact() throws Exception {
        try (
            Servers servers = new Servers(
                new ServerH2(
                    new ScriptSql(
                        new ResourceOf(
                            new Joined(
                                "/",
                                "com/github/fabriciofx/cactoos/jdbc/phonebook",
                                "phonebook-h2.sql"
                            )
                        )
                    )
                ),
                new ServerPgsql(
                    new ScriptSql(
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
                final Phonebook phonebook = new PhonebookSql(session);
                final Contact contact = phonebook.contact(
                    new MapOf<String, String>(
                        new MapEntry<>("name", "Donald Knuth")
                    )
                );
                contact.phones().add(
                    new MapOf<String, String>(
                        new MapEntry<>("number", "99991234"),
                        new MapEntry<>("carrier", "TIM")
                    )
                );
                contact.phones().add(
                    new MapOf<String, String>(
                        new MapEntry<>("number", "98812564"),
                        new MapEntry<>("carrier", "Oi")
                    )
                );
                MatcherAssert.assertThat(
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
    public void findContact() throws Exception {
        try (
            Servers servers = new Servers(
                new ServerH2(
                    new ScriptSql(
                        new ResourceOf(
                            new Joined(
                                "/",
                                "com/github/fabriciofx/cactoos/jdbc/phonebook",
                                "phonebook-h2.sql"
                            )
                        )
                    )
                ),
                new ServerPgsql(
                    new ScriptSql(
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
                    XhtmlMatchers.xhtml(
                        new PhonebookSql(session)
                            .filter("maria")
                            .iterator()
                            .next()
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
    public void renameContact() throws Exception {
        try (
            Servers servers = new Servers(
                new ServerH2(
                    new ScriptSql(
                        new ResourceOf(
                            new Joined(
                                "/",
                                "com/github/fabriciofx/cactoos/jdbc/phonebook",
                                "phonebook-h2.sql"
                            )
                        )
                    )
                ),
                new ServerPgsql(
                    new ScriptSql(
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
                final Phonebook phonebook = new PhonebookSql(session);
                final Contact contact = phonebook.filter("maria").iterator()
                    .next();
                contact.update(
                    new MapOf<String, String>(
                        new MapEntry<>("name", "Maria Lima")
                    )
                );
                MatcherAssert.assertThat(
                    XhtmlMatchers.xhtml(
                        new PhonebookSql(session)
                            .filter("maria")
                            .iterator()
                            .next()
                            .about()
                    ),
                    XhtmlMatchers.hasXPaths(
                        "/contact/name[text()='Maria Lima']"
                    )
                );
            }
        }
    }
}
