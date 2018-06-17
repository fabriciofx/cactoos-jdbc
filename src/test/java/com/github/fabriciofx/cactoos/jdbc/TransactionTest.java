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
package com.github.fabriciofx.cactoos.jdbc;

import com.github.fabriciofx.cactoos.jdbc.agenda.Contact;
import com.github.fabriciofx.cactoos.jdbc.agenda.SqlContacts;
import com.github.fabriciofx.cactoos.jdbc.script.SqlScript;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuthSession;
import com.github.fabriciofx.cactoos.jdbc.session.TransactedSession;
import com.github.fabriciofx.cactoos.jdbc.stmt.Transaction;
import org.cactoos.io.ResourceOf;
import org.junit.Test;

/**
 * Transaction tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 */
public final class TransactionTest {
    @Test
    public void agenda() throws Exception {
        final TransactedSession session = new TransactedSession(
            new NoAuthSession(
                new H2Source("testdb")
            )
        );
        new SqlScript(
            session,
            new ResourceOf(
                "com/github/fabriciofx/cactoos/jdbc/agenda/agendadb.sql"
            )
        ).exec();
        new Transaction<>(
            session,
            () -> {
                final Contact einstein = new SqlContacts(session)
                    .contact("Albert Einstein");
                einstein.phones().phone("912232325", "TIM");
                einstein.phones().phone("982231234", "Oi");
                System.out.println(einstein.asString());
                return einstein;
            }
        ).result();
    }
}
