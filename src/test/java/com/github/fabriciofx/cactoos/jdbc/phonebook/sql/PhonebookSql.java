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
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contacts;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phonebook;
import com.github.fabriciofx.cactoos.jdbc.query.SimpleQuery;
import com.github.fabriciofx.cactoos.jdbc.query.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.query.param.UuidParam;
import com.github.fabriciofx.cactoos.jdbc.stmt.Insert;
import java.util.Map;
import java.util.UUID;

/**
 * Phonebook for SQL.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.4
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class PhonebookSql implements Phonebook {
    /**
     * Session.
     */
    private final Session session;

    /**
     * Ctor.
     * @param sssn The Session
     */
    public PhonebookSql(final Session sssn) {
        this.session = sssn;
    }

    @Override
    public Contact contact(
        final Map<String, String> properties
    ) throws Exception {
        final UUID id = UUID.randomUUID();
        new Insert(
            this.session,
            new SimpleQuery(
                "INSERT INTO contact (id, name) VALUES (:id, :name)",
                new UuidParam("id", id),
                new TextParam("name", properties.get("name"))
            )
        ).result();
        return new ContactSql(this.session, id);
    }

    @Override
    public Contacts filter(final String name) throws Exception {
        return new ContactsSqlFiltered(this.session, name);
    }
}
