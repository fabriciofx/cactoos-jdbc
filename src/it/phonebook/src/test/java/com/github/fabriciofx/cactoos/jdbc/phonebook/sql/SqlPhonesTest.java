/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
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
 * {@link SqlPhones} tests.
 *
 * @since 0.1
 */
final class SqlPhonesTest {
    @Test
    void getsPhoneAtIndex() throws Exception {
        try (
            Server<DataSource> server = new H2Server(
                new SqlScript(
                    new ResourceOf("phonebook/phonebook-h2.sql")
                )
            )
        ) {
            server.start();
            final Contact contact = new SqlPhonebook(
                new NoAuth(server.resource())
            ).create("Edsger Dijkstra");
            contact.phones().add("95550003");
            contact.phones().add("91110001");
            contact.phones().add("93330002");
            contact.phones().get(1).delete();
            new Assertion<>(
                "must get the phone at the given index in number order",
                XhtmlMatchers.xhtml(contact.xml()),
                XhtmlMatchers.hasXPath(
                    "/contact/phones[count(phone)=2 and not(phone/number='93330002')]"
                )
            ).affirm();
        }
    }
}
