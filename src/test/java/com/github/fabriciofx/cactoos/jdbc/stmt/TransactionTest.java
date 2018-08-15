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
package com.github.fabriciofx.cactoos.jdbc.stmt;

import com.github.fabriciofx.cactoos.jdbc.agenda.Contact;
import com.github.fabriciofx.cactoos.jdbc.agenda.SqlContacts;
import com.github.fabriciofx.cactoos.jdbc.result.ResultAsValue;
import com.github.fabriciofx.cactoos.jdbc.script.SqlScriptFromInput;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuthSession;
import com.github.fabriciofx.cactoos.jdbc.session.TransactedSession;
import com.github.fabriciofx.cactoos.jdbc.source.H2Source;
import org.cactoos.io.ResourceOf;
import org.cactoos.text.JoinedText;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Transaction tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class TransactionTest {
    @Test
    public void agenda() throws Exception {
        final TransactedSession transacted = new TransactedSession(
            new NoAuthSession(
                new H2Source("testdb")
            )
        );
        new SqlScriptFromInput(
            new ResourceOf(
                "com/github/fabriciofx/cactoos/jdbc/agenda/agendadb-h2.sql"
            )
        ).exec(transacted);
        MatcherAssert.assertThat(
            "Can't perform a transaction",
            new ResultAsValue<>(
                new Transaction<>(
                    transacted,
                    () -> {
                        final Contact contact = new SqlContacts(transacted)
                            .contact("Albert Einstein");
                        contact.phones().phone("912232325", "TIM");
                        contact.phones().phone("982231234", "Oi");
                        return contact.asString();
                    }
                ),
                String.class
            ).value(),
            Matchers.containsString(
                new JoinedText(
                    "\n",
                    "Name: Albert Einstein",
                    "Phone: 912232325 (TIM)",
                    "Phone: 982231234 (Oi)"
                ).asString()
            )
        );
    }
}
