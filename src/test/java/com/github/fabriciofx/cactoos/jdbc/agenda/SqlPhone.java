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
import com.github.fabriciofx.cactoos.jdbc.query.SimpleQuery;
import com.github.fabriciofx.cactoos.jdbc.query.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.query.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.query.param.UuidParam;
import com.github.fabriciofx.cactoos.jdbc.result.ResultAsValues;
import com.github.fabriciofx.cactoos.jdbc.stmt.Select;
import com.github.fabriciofx.cactoos.jdbc.stmt.Update;
import java.util.List;
import java.util.UUID;
import org.cactoos.Scalar;
import org.cactoos.text.JoinedText;

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
    private final UUID contact;

    /**
     * Sequential number.
     */
    private final int seq;

    /**
     * Ctor.
     * @param sssn A Session
     * @param contact Contact's ID
     * @param seq Sequential number
     */
    public SqlPhone(final Session sssn, final UUID contact, final int seq) {
        this.session = sssn;
        this.contact = contact;
        this.seq = seq;
    }

    @Override
    public String number() throws Exception {
        final Scalar<List<String>> numbers = new ResultAsValues<>(
            new Select(
                this.session,
                new SimpleQuery(
                    new JoinedText(
                        " ",
                        "SELECT number FROM phone WHERE (contact = :contact)",
                        "AND (seq = :seq)"
                    ),
                    new UuidParam("contact", this.contact),
                    new IntParam("seq", this.seq)
                )
            )
        );
        return numbers.value().get(0);
    }

    @Override
    public String carrier() throws Exception {
        final Scalar<List<String>> carriers = new ResultAsValues<>(
            new Select(
                this.session,
                new SimpleQuery(
                    new JoinedText(
                        " ",
                        "SELECT carrier FROM phone WHERE (contact = :contact)",
                        "AND (seq = :seq)"
                    ),
                    new UuidParam("contact", this.contact),
                    new IntParam("seq", this.seq)
                )
            )
        );
        return carriers.value().get(0);
    }

    @Override
    public void delete() throws Exception {
        new Update(
            this.session,
            new SimpleQuery(
                "DELETE FROM phone WHERE (contact = :contact) AND (seq = :seq)",
                new UuidParam("contact", this.contact),
                new IntParam("seq", this.seq)
            )
        ).result();
    }

    @Override
    public void change(
        final String number,
        final String carrier
    ) throws Exception {
        new Update(
            this.session,
            new SimpleQuery(
                new JoinedText(
                    " ",
                    "UPDATE phone SET number = :number, carrier = :carrier",
                    "WHERE (contact = :contact) AND (seq = :seq)"
                ),
                new TextParam("number", number),
                new TextParam("carrier", carrier),
                new UuidParam("contact", this.contact),
                new IntParam("seq", this.seq)
            )
        ).result();
    }
}
