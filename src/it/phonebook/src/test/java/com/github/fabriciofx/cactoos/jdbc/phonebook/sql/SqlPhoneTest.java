/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phone;
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
 * {@link SqlPhone} tests.
 *
 * @since 0.1
 */
final class SqlPhoneTest {
    @Test
    void updatesPhoneNumber() throws Exception {
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
            ).create("Grace Hopper");
            contact.phones().add("(87) 98123-6547");
            contact.phones().get(0).update("(87) 99777-0001");
            new Assertion<>(
                "must replace the old phone number with the new one",
                XhtmlMatchers.xhtml(contact.xml()),
                XhtmlMatchers.hasXPath(
                    "/contact/phones[count(phone)=1]/phone/number[text()='(87) 99777-0001']"
                )
            ).affirm();
        }
    }

    @Test
    void retrievesOnlyItsOwnNumberAsXml() throws Exception {
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
            ).create("Barbara Liskov");
            contact.phones().add("(87) 99666-0002");
            contact.phones().add("(87) 99444-0001");
            new Assertion<>(
                "must not include numbers of other phones of the contact",
                XhtmlMatchers.xhtml(contact.phones().get(0).xml()),
                XhtmlMatchers.hasXPath(
                    "/phones[count(phone)=1]/phone/number[text()='(87) 99444-0001']"
                )
            ).affirm();
        }
    }

    @Test
    void keepsRepresentingPhoneAfterUpdate() throws Exception {
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
            ).create("Ken Thompson");
            contact.phones().add("(87) 99222-0004");
            final Phone phone = contact.phones().get(0);
            phone.update("(87) 99888-0007");
            new Assertion<>(
                "must not lose track of the phone after updating its number",
                XhtmlMatchers.xhtml(phone.xml()),
                XhtmlMatchers.hasXPath(
                    "/phones/phone/number[text()='(87) 99888-0007']"
                )
            ).affirm();
        }
    }
}
