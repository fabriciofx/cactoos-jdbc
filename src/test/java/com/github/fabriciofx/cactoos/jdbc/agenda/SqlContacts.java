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
package com.github.fabriciofx.cactoos.jdbc.agenda;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.query.KeyedQuery;
import com.github.fabriciofx.cactoos.jdbc.query.SimpleQuery;
import com.github.fabriciofx.cactoos.jdbc.query.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.result.ResultAsValues;
import com.github.fabriciofx.cactoos.jdbc.stmt.InsertWithKeys;
import com.github.fabriciofx.cactoos.jdbc.stmt.Select;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.cactoos.text.JoinedText;

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
        "PMD.AvoidCatchingGenericException",
        "PMD.AvoidInstantiatingObjectsInLoops",
        "PMD.AvoidDuplicateLiterals",
        "PMD.AvoidThrowingRawExceptionTypes"
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
    public Contact contact(final String name) throws Exception {
        final UUID id = new ResultAsValues<>(
            new InsertWithKeys(
                this.session,
                new KeyedQuery(
                    "INSERT INTO contact (name) VALUES (:name)",
                    new TextParam("name", name)
                )
            ),
            UUID.class
        ).value().get(0);
        return new SqlContact(this.session, id);
    }

    @Override
    public List<Contact> find(final String name) throws Exception {
        final List<UUID> ids = new ResultAsValues<>(
            new Select(
                this.session,
                new SimpleQuery(
                    new JoinedText(
                        " ",
                        "SELECT id FROM contact WHERE name ILIKE",
                        "'%' || :name || '%'"
                    ),
                    new TextParam("name", name)
                )
            ),
            UUID.class
        ).value();
        final List<Contact> founds = new LinkedList<>();
        for (final UUID id : ids) {
            founds.add(new SqlContact(this.session, id));
        }
        return founds;
    }

    @Override
    public Iterator<Contact> iterator() {
        try {
            final List<UUID> ids = new ResultAsValues<>(
                new Select(
                    this.session,
                    new SimpleQuery(
                        "SELECT id FROM contact"
                    )
                ),
                UUID.class
            ).value();
            final List<Contact> list = new LinkedList<>();
            for (final UUID id : ids) {
                list.add(new SqlContact(this.session, id));
            }
            return list.iterator();
            // @checkstyle IllegalCatchCheck (1 line)
        } catch (final Exception ex) {
            throw new RuntimeException("Error in contacts iterator", ex);
        }
    }
}
