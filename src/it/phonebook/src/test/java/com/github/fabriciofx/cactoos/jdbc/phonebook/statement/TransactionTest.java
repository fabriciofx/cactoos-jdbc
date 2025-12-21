/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.statement;

import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phonebook;
import com.github.fabriciofx.cactoos.jdbc.phonebook.sql.SqlPhonebook;
import com.github.fabriciofx.cactoos.jdbc.result.ResultAsValue;
import com.github.fabriciofx.cactoos.jdbc.script.ScriptOf;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuth;
import com.github.fabriciofx.cactoos.jdbc.session.Transacted;
import com.github.fabriciofx.cactoos.jdbc.statement.Transaction;
import com.github.fabriciofx.fake.server.db.source.H2Source;
import com.jcabi.matchers.XhtmlMatchers;
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
 */
@SuppressWarnings({"PMD.UnnecessaryLocalRule", "PMD.EmptyCatchBlock"})
final class TransactionTest {
    @Test
    void commit() throws Exception {
        final Transacted transacted = new Transacted(
            new NoAuth(
                new H2Source("safedb")
            )
        );
        new ScriptOf(
            new ResourceOf(
                "phonebook/phonebook-h2.sql"
            )
        ).run(transacted);
        new Assertion<>(
            "must perform a transaction commit",
            XhtmlMatchers.xhtml(
                new ResultAsValue<>(
                    new Transaction<>(
                        transacted,
                        () -> {
                            final Phonebook phonebook = new SqlPhonebook(
                                transacted
                            );
                            final Contact contact = phonebook.contact(
                                "Albert Einstein"
                            );
                            contact.phones().add("99991234", "TIM");
                            contact.phones().add("98812564", "Oi");
                            return contact.about();
                        }
                    )
                ).value()
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

    @Test
    void rollback() throws Exception {
        final Transacted transacted = new Transacted(
            new NoAuth(
                new H2Source("unsafedb")
            )
        );
        new ScriptOf(
            new ResourceOf(
                "phonebook/phonebook-h2.sql"
            )
        ).run(transacted);
        final Phonebook phonebook = new SqlPhonebook(transacted);
        final String name = "Frank Miller";
        try {
            new Transaction<>(
                transacted,
                () -> {
                    final Contact contact = phonebook.contact(name);
                    contact.phones().add("99991234", "TIM");
                    throw new IllegalStateException("Rollback");
                }
            ).result();
        } catch (final IllegalStateException ex) {
        }
        new Assertion<>(
            "must perform a transaction rollback",
            () -> phonebook.search(name).page(0).content().size(),
            new HasValue<>(0)
        ).affirm();
    }
}
