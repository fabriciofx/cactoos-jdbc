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
package com.github.fabriciofx.cactoos.jdbc.agenda;

import com.github.fabriciofx.cactoos.jdbc.Servers;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.script.OldSqlScriptFromInput;
import com.github.fabriciofx.cactoos.jdbc.script.SqlScriptFromInput;
import com.github.fabriciofx.cactoos.jdbc.server.H2Server;
import com.github.fabriciofx.cactoos.jdbc.server.MySqlServer;
import org.cactoos.io.ResourceOf;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.llorllale.cactoos.matchers.TextHasString;

/**
 * Agenda tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class AgendaTest {
    @Test
    public void addContact() throws Exception {
        try (
            final Servers servers = new Servers(
//                new H2Server(
//                    new OldSqlScriptFromInput(
//                        new ResourceOf(
//                            "com/github/fabriciofx/cactoos/jdbc/agenda/agendadb-h2.sql"
//                        )
//                    )
//                )
                new MySqlServer(
                    new OldSqlScriptFromInput(
                        new ResourceOf(
                            "com/github/fabriciofx/cactoos/jdbc/agenda/agendadb-mysql.sql"
                        )
                    )
                )
            )
        ) {
            for (final Session session : servers.sessions()) {
                final Contacts contacts = new SqlContacts(session);
                final Contact contact = contacts.contact("Donald Knuth");
                contact.phones().phone("99991234", "TIM");
                contact.phones().phone("98812564", "Oi");
                MatcherAssert.assertThat(
                    "Can't add an agenda contact",
                    contact,
                    new TextHasString(
                        String.join(
                            "\n",
                            "Name: Donald Knuth",
                            "Phone: 99991234 (TIM)",
                            "Phone: 98812564 (Oi)"
                        )
                    )
                );
            }
        }
    }

    @Test
    public void findContact() throws Exception {
        try (
            final Servers servers = new Servers(
                new H2Server(
                    new SqlScriptFromInput(
                        new ResourceOf(
                            "com/github/fabriciofx/cactoos/jdbc/agenda/agendadb-h2.sql"
                        )
                    )
                ),
                new MySqlServer(
                    new SqlScriptFromInput(
                        new ResourceOf(
                            "com/github/fabriciofx/cactoos/jdbc/agenda/agendadb-mysql.sql"
                        )
                    )
                )
            )
        ) {
            for (final Session session : servers.sessions()) {
                MatcherAssert.assertThat(
                    "Can't find an agenda contact",
                    new SqlContacts(session).find("maria").get(0),
                    new TextHasString("Name: Maria Souza")
                );
            }
        }
    }

    @Test
    public void renameContact() throws Exception {
        try (
            final Servers servers = new Servers(
                new H2Server(
                    new SqlScriptFromInput(
                        new ResourceOf(
                            "com/github/fabriciofx/cactoos/jdbc/agenda/agendadb-h2.sql"
                        )
                    )
                ),
                new MySqlServer(
                    new SqlScriptFromInput(
                        new ResourceOf(
                            "com/github/fabriciofx/cactoos/jdbc/agenda/agendadb-mysql.sql"
                        )
                    )
                )
            )
        ) {
            for (final Session session : servers.sessions()) {
                final Contact contact = new SqlContacts(session).find("maria").get(0);
                contact.rename("Maria Lima");
                MatcherAssert.assertThat(
                    "Can't rename an agenda contact",
                    new SqlContacts(session).find("maria").get(0),
                    new TextHasString("Name: Maria Lima")
                );
            }
        }
    }
}
