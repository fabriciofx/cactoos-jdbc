/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.statement;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phonebook;
import com.github.fabriciofx.cactoos.jdbc.phonebook.sql.SqlPhonebook;
import com.github.fabriciofx.cactoos.jdbc.source.NoAuth;
import com.github.fabriciofx.cactoos.jdbc.source.Transacted;
import com.github.fabriciofx.cactoos.jdbc.statement.Transaction;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.script.SqlScript;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import com.jcabi.matchers.XhtmlMatchers;
import javax.sql.DataSource;
import org.cactoos.io.ResourceOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * Transaction tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @checkstyle NestedTryDepthCheck (500 lines)
 */
@SuppressWarnings({
    "PMD.UnnecessaryLocalRule",
    "PMD.EmptyCatchBlock",
    "PMD.ExceptionAsFlowControl"
})
final class TransactionTest {
    @Test
    void commitsATransaction() throws Exception {
        final String name = "Albert Einstein";
        try (
            Server<DataSource> server = new H2Server(
                new SqlScript(
                    new ResourceOf("phonebook/phonebook-h2.sql")
                )
            )
        ) {
            server.start();
            final Source source = new NoAuth(server.resource());
            final Source transacted = new Transacted(source);
            try (Connexio connexio = transacted.connexio()) {
                new Transaction<>(
                    connexio,
                    () -> {
                        final Phonebook phonebook = new SqlPhonebook(transacted);
                        final Contact contact = phonebook.create(name);
                        contact.phones().add("99991234", "TIM");
                        contact.phones().add("98812564", "Oi");
                        return true;
                    }
                ).execute();
            }
            new Assertion<>(
                "must perform a transaction commit",
                XhtmlMatchers.xhtml(
                    new SqlPhonebook(source).search(name).get(0).about()
                ),
                XhtmlMatchers.hasXPaths(
                    "/contact/name[text()='Albert Einstein']",
                    "/contact/phones/phone/number[text()='99991234']",
                    "/contact/phones/phone/carrier[text()='TIM']",
                    "/contact/phones/phone/number[text()='98812564']",
                    "/contact/phones/phone/carrier[text()='Oi']"
                )
            ).affirm();
        }
    }

    @Test
    void rollbackATransaction() throws Exception {
        final String name = "Frank Miller";
        try (
            Server<DataSource> server = new H2Server(
                new SqlScript(
                    new ResourceOf("phonebook/phonebook-h2.sql")
                )
            )
        ) {
            server.start();
            final Source source = new NoAuth(server.resource());
            final Source transacted = new Transacted(source);
            try (Connexio connexio = transacted.connexio()) {
                try {
                    new Transaction<>(
                        connexio,
                        () -> {
                            final Phonebook phonebook =
                                new SqlPhonebook(transacted);
                            final Contact contact = phonebook.create(name);
                            contact.phones().add("88884321", "Vivo");
                            contact.phones().add("97824562", "Claro");
                            throw new IllegalStateException("Rollback");
                        }
                    ).execute();
                } catch (final IllegalStateException ex) {
                }
            }
            new Assertion<>(
                "must perform a transaction rollback",
                () -> new SqlPhonebook(source).search(name).size(),
                new HasValue<>(0)
            ).affirm();
        }
    }
}
