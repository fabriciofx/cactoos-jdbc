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
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.param.UuidOf;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phone;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import java.util.UUID;
import org.cactoos.text.Joined;

/**
 * Phone for SQL.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class SqlPhone implements Phone {
    /**
     * Session.
     */
    private final Session session;

    /**
     * Contact's ID.
     */
    private final UUID id;

    /**
     * Phone num.
     */
    private final String num;

    /**
     * Ctor.
     * @param sssn A Session
     * @param contact A Contact's ID
     * @param number Phone number
     */
    public SqlPhone(
        final Session sssn,
        final UUID contact,
        final String number
    ) {
        this.session = sssn;
        this.id = contact;
        this.num = number;
    }

    @Override
    public void delete() throws Exception {
        new Update(
            this.session,
            new QueryOf(
                new Joined(
                    " ",
                    "DELETE FROM phone WHERE (contact_id = :contact_id)",
                    "AND (number = :number)"
                ),
                new UuidOf("contact_id", this.id),
                new TextOf("number", this.num)
            )
        ).result();
    }

    @Override
    public void update(
        final String number,
        final String carrier
    ) throws Exception {
        new Update(
            this.session,
            new QueryOf(
                new Joined(
                    " ",
                    "UPDATE phone SET number = :number, carrier = :carrier",
                    "WHERE (contact_id = :contact_id) AND (number = :number)"
                ),
                new TextOf("number", number),
                new TextOf("carrier", carrier),
                new UuidOf("contact_id", this.id),
                new TextOf("number", this.num)
            )
        ).result();
    }
}
