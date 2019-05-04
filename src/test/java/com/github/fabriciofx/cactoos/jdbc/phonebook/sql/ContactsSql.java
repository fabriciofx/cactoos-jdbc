/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Fabricio Barros Cabral
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
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contacts;
import com.github.fabriciofx.cactoos.jdbc.query.QuerySimple;
import com.github.fabriciofx.cactoos.jdbc.rset.ResultSetAsValue;
import com.github.fabriciofx.cactoos.jdbc.rset.ResultSetAsValues;
import com.github.fabriciofx.cactoos.jdbc.stmt.StatementSelect;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.cactoos.iterator.Mapped;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.FormattedText;

/**
 * Contacts for SQL.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings(
    {
        "PMD.AvoidInstantiatingObjectsInLoops",
        "PMD.AvoidDuplicateLiterals",
        "PMD.AvoidThrowingRawExceptionTypes",
        "PMD.UseLocaleWithCaseConversions"
    }
)
public final class ContactsSql implements Contacts {
    /**
     * Session.
     */
    private final Session session;

    /**
     * Ctor.
     * @param sssn A Session.
     */
    public ContactsSql(final Session sssn) {
        this.session = sssn;
    }

    @Override
    public int count() throws Exception {
        return new ResultSetAsValue<Integer>(
            new StatementSelect(
                this.session,
                new QuerySimple("SELECT COUNT(name) FROM contact")
            )
        ).value();
    }

    @Override
    public Contact get(final int index) throws Exception {
        return new ContactSql(
            this.session,
            new ResultSetAsValue<UUID>(
                new StatementSelect(
                    this.session,
                    new QuerySimple(
                        new FormattedText(
                            "SELECT id FROM contact FETCH FIRST %d ROWS ONLY",
                            index
                        )
                    )
                )
            ).value()
        );
    }

    @Override
    public Iterator<Contact> iterator() {
        return new Mapped<>(
            id -> new ContactSql(this.session, id),
            new Unchecked<List<UUID>>(
                new ResultSetAsValues<>(
                    new StatementSelect(
                        this.session,
                        new QuerySimple("SELECT id FROM contact")
                    )
                )
            ).value().iterator()
        );
    }
}
