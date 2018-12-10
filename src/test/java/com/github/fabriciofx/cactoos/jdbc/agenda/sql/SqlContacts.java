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
package com.github.fabriciofx.cactoos.jdbc.agenda.sql;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.agenda.Contact;
import com.github.fabriciofx.cactoos.jdbc.agenda.Contacts;
import com.github.fabriciofx.cactoos.jdbc.query.SimpleQuery;
import com.github.fabriciofx.cactoos.jdbc.rset.ResultSetAsValue;
import com.github.fabriciofx.cactoos.jdbc.rset.ResultSetAsValues;
import com.github.fabriciofx.cactoos.jdbc.stmt.Select;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.cactoos.Scalar;
import org.cactoos.scalar.UncheckedScalar;
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
public final class SqlContacts implements Contacts {
    /**
     * Session.
     */
    private final Session session;

    /**
     * Ctor.
     * @param sssn A Session.
     */
    public SqlContacts(final Session sssn) {
        this.session = sssn;
    }

    @Override
    public int count() throws Exception {
        return new ResultSetAsValue<Integer>(
            new Select(
                this.session,
                new SimpleQuery("SELECT COUNT(name) FROM contact")
            )
        ).value();
    }

    @Override
    public Contact get(final int index) throws Exception {
        final Scalar<UUID> id = new ResultSetAsValue<>(
            new Select(
                this.session,
                new SimpleQuery(
                    new FormattedText(
                        "SELECT id FROM contact FETCH FIRST %d ROWS ONLY",
                        index
                    )
                )
            )
        );
        return new SqlContact(this.session, id.value());
    }

    @Override
    public Iterator<Contact> iterator() {
        final UncheckedScalar<List<UUID>> ids = new UncheckedScalar<>(
            new ResultSetAsValues<>(
                new Select(
                    this.session,
                    new SimpleQuery(
                        "SELECT id FROM contact"
                    )
                )
            )
        );
        final List<Contact> list = new LinkedList<>();
        for (final UUID id : ids.value()) {
            list.add(new SqlContact(this.session, id));
        }
        return list.iterator();
    }
}
