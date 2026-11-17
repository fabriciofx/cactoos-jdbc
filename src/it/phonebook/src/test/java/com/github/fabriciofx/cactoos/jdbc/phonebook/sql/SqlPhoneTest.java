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
            contact.phones().add("81236547");
            contact.phones().get(0).update("97770001");
            new Assertion<>(
                "must replace the old phone number with the new one",
                XhtmlMatchers.xhtml(contact.xml()),
                XhtmlMatchers.hasXPath(
                    "/contact/phones[count(phone)=1]/phone/number[text()='97770001']"
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
            contact.phones().add("96660002");
            contact.phones().add("94440001");
            new Assertion<>(
                "must not include numbers of other phones of the contact",
                XhtmlMatchers.xhtml(contact.phones().get(0).xml()),
                XhtmlMatchers.hasXPath(
                    "/phones[count(phone)=1]/phone/number[text()='94440001']"
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
            contact.phones().add("92220004");
            final Phone phone = contact.phones().get(0);
            phone.update("98880007");
            new Assertion<>(
                "must not lose track of the phone after updating its number",
                XhtmlMatchers.xhtml(phone.xml()),
                XhtmlMatchers.hasXPath(
                    "/phones/phone/number[text()='98880007']"
                )
            ).affirm();
        }
    }
}
