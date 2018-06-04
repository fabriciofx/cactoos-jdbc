/**
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.fabriciofx.cactoos.jdbc.agenda;

import com.github.fabriciofx.cactoos.jdbc.Result;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.adapter.ResultSetToType;
import com.github.fabriciofx.cactoos.jdbc.adapter.ResultSetToTypes;
import com.github.fabriciofx.cactoos.jdbc.query.KeyedQuery;
import com.github.fabriciofx.cactoos.jdbc.query.NamedQuery;
import com.github.fabriciofx.cactoos.jdbc.stmt.InsertWithKeys;
import com.github.fabriciofx.cactoos.jdbc.stmt.Select;
import com.github.fabriciofx.cactoos.jdbc.value.TextValue;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version Id
 * @since
 */
public final class SqlContacts implements Contacts {
    private final Session session;

    public SqlContacts(final Session sssn) {
        this.session = sssn;
    }

    @Override
    public Contact contact(final String name) throws Exception {
        final UUID id = new ResultSetToType<>(
            new Result<>(
                this.session,
                new InsertWithKeys(
                    new KeyedQuery(
                        "INSERT INTO contact (name) VALUES (:name)",
                        new TextValue("name", name)
                    )
                )
            ),
            UUID.class
        ).value();
        return new SqlContact(this.session, id);
    }

    @Override
    public List<Contact> find(final String name) throws Exception {
        final List<UUID> ids = new ResultSetToTypes<>(
            new Result<>(
                this.session,
                new Select(
                    new NamedQuery(
                        "SELECT id FROM contact WHERE name ILIKE '%' || :name || '%'",
                        new TextValue("name", name)
                    )
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
            final List<UUID> ids = new ResultSetToTypes<>(
                new Result<>(
                    this.session,
                    new Select(
                        new NamedQuery(
                            "SELECT id FROM contact"
                        )
                    )
                ),
                UUID.class
            ).value();
            final List<Contact> list = new LinkedList<>();
            for (final UUID id : ids) {
                list.add(new SqlContact(this.session, id));
            }
            return list.iterator();
        } catch (final Exception ex) {
            throw new RuntimeException("Error in interator of contacts");
        }
    }
}
