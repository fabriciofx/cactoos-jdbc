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

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.result.ResultToValue;
import com.github.fabriciofx.cactoos.jdbc.result.ResultToValues;
import com.github.fabriciofx.cactoos.jdbc.query.KeyedQuery;
import com.github.fabriciofx.cactoos.jdbc.query.NamedQuery;
import com.github.fabriciofx.cactoos.jdbc.stmt.InsertWithKeys;
import com.github.fabriciofx.cactoos.jdbc.stmt.Select;
import com.github.fabriciofx.cactoos.jdbc.value.AnyValue;
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
public final class SqlPhones implements Phones {
    private final Session session;
    private final UUID contact;

    public SqlPhones(final Session sssn, final UUID contact) {
        this.session = sssn;
        this.contact = contact;
    }

    @Override
    public Phone phone(final String number, final String operator) throws Exception{
        final Integer seq = new ResultToValue<>(
            new InsertWithKeys(
                this.session,
                new KeyedQuery(
                    "INSERT INTO phone (contact, number, operator) VALUES (:contact, :number, :operator)",
                    new AnyValue("contact", this.contact),
                    new TextValue("number", number),
                    new TextValue("operator", operator)
                )
            ),
            Integer.class
        ).value();
        return new SqlPhone(this.session, this.contact, seq);
    }

    @Override
    public Iterator<Phone> iterator() {
        try {
            final List<Integer> seqs = new ResultToValues<>(
                new Select(
                    this.session,
                    new NamedQuery(
                        "SELECT seq FROM phone WHERE contact = :contact",
                        new AnyValue("contact", this.contact)
                    )
                ),
                Integer.class
            ).value();
            final List<Phone> list = new LinkedList<>();
            for (final int seq : seqs) {
                list.add(new SqlPhone(this.session, this.contact, seq));
            }
            return list.iterator();
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
