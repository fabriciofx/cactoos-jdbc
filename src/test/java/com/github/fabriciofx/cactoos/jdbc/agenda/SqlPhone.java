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
import com.github.fabriciofx.cactoos.jdbc.query.NamedQuery;
import com.github.fabriciofx.cactoos.jdbc.result.ResultAsValues;
import com.github.fabriciofx.cactoos.jdbc.stmt.Select;
import com.github.fabriciofx.cactoos.jdbc.stmt.Update;
import com.github.fabriciofx.cactoos.jdbc.value.AnyValue;
import com.github.fabriciofx.cactoos.jdbc.value.IntValue;
import com.github.fabriciofx.cactoos.jdbc.value.TextValue;
import java.util.UUID;

/**
 * Phone for SQL.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 */
public final class SqlPhone implements Phone {
    private final Session session;
    private final UUID contact;
    private final int seq;

    public SqlPhone(final Session sssn, final UUID contact, final int seq) {
        this.session = sssn;
        this.contact = contact;
        this.seq = seq;
    }

    @Override
    public String number() throws Exception {
        return new ResultAsValues<>(
            new Select(
                this.session,
                new NamedQuery(
                    "SELECT number FROM phone WHERE (contact = :contact) AND (seq = :seq)",
                    new AnyValue("contact", this.contact),
                    new IntValue("seq", this.seq)
                )
            ),
            String.class
        ).value().get(0);
    }

    @Override
    public String operator() throws Exception {
        return new ResultAsValues<>(
            new Select(
                this.session,
                new NamedQuery(
                    "SELECT operator FROM phone WHERE (contact = :contact) AND (seq = :seq)",
                    new AnyValue("contact", this.contact),
                    new IntValue("seq", this.seq)
                )
            ),
            String.class
        ).value().get(0);
    }

    @Override
    public void delete() throws Exception {
        new Update(
            this.session,
            new NamedQuery(
                "DELETE FROM phone WHERE (contact = :contact) AND (seq = :seq)",
                new AnyValue("contact", this.contact),
                new IntValue("seq", this.seq)
            )
        ).result();
    }

    @Override
    public void change(
        final String number,
        final String operator
    ) throws Exception {
        new Update(
            this.session,
            new NamedQuery(
                "UPDATE phone SET number = :number, operator = :operator " +
                    "WHERE (contact = :contact) AND (seq = :seq)",
                new TextValue("number", number),
                new TextValue("operator", operator),
                new AnyValue("contact", this.contact),
                new IntValue("seq", this.seq)
            )
        ).result();
    }
}
