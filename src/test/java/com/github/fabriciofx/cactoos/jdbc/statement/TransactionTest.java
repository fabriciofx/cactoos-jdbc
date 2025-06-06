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
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phonebook;
import com.github.fabriciofx.cactoos.jdbc.phonebook.sql.SqlPhonebook;
import com.github.fabriciofx.cactoos.jdbc.result.ResultAsValue;
import com.github.fabriciofx.cactoos.jdbc.script.ScriptOf;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuth;
import com.github.fabriciofx.cactoos.jdbc.session.Transacted;
import com.github.fabriciofx.cactoos.jdbc.source.H2Source;
import com.jcabi.matchers.XhtmlMatchers;
import java.util.stream.StreamSupport;
import org.cactoos.io.ResourceOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * StatementTransaction tests.
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
        "PMD.EmptyCatchBlock",
        "PMD.TestClassWithoutTestCases"
    }
)
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
                "com/github/fabriciofx/cactoos/jdbc/phonebook/phonebook-h2.sql"
            )
        ).run(transacted);
        MatcherAssert.assertThat(
            "Can't perform a transaction commit",
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
        );
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
                "com/github/fabriciofx/cactoos/jdbc/phonebook/phonebook-h2.sql"
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
        MatcherAssert.assertThat(
            "Can't perform a transaction rollback",
            StreamSupport.stream(
                phonebook.search(name).page(0).content().spliterator(),
                false
            ).count(),
            Matchers.equalTo(0L)
        );
    }
}
