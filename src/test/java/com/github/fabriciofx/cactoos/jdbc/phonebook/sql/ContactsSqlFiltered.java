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
import com.github.fabriciofx.cactoos.jdbc.param.ParamText;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contacts;
import com.github.fabriciofx.cactoos.jdbc.query.QuerySimple;
import com.github.fabriciofx.cactoos.jdbc.rset.ResultSetAsValues;
import com.github.fabriciofx.cactoos.jdbc.stmt.StatementSelect;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.cactoos.Scalar;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.Joined;
import org.cactoos.text.Lowered;

/**
 * Filtered Contacts for SQL.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.4
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class ContactsSqlFiltered implements Contacts {
    /**
     * Session.
     */
    private final Session session;

    /**
     * Ids.
     */
    private final Scalar<List<UUID>> ids;

    /**
     * Ctor.
     * @param sssn A Session.
     * @param name Contact's name to be filtered.
     */
    public ContactsSqlFiltered(final Session sssn, final String name) {
        this.session = sssn;
        this.ids = new ResultSetAsValues<>(
            new StatementSelect(
                sssn,
                new QuerySimple(
                    new Joined(
                        " ",
                        "SELECT id FROM contact WHERE LOWER(name) LIKE",
                        "'%' || :name || '%'"
                    ),
                    new ParamText("name", new Lowered(name))
                )
            )
        );
    }

    @Override
    public int count() throws Exception {
        return this.ids.value().size();
    }

    @Override
    public Contact get(final int index) throws Exception {
        return new ContactSql(this.session, this.ids.value().get(index));
    }

    @Override
    public Iterator<Contact> iterator() {
        final List<Contact> list = new LinkedList<>();
        for (final UUID id : new Unchecked<>(this.ids).value()) {
            list.add(new ContactSql(this.session, id));
        }
        return list.iterator();
    }
}
