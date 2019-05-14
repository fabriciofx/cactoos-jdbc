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
import com.github.fabriciofx.cactoos.jdbc.param.ParamUuid;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phone;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phones;
import com.github.fabriciofx.cactoos.jdbc.query.QuerySimple;
import com.github.fabriciofx.cactoos.jdbc.rset.ResultSetAsValue;
import com.github.fabriciofx.cactoos.jdbc.rset.ResultSetAsValues;
import com.github.fabriciofx.cactoos.jdbc.statement.StatementInsert;
import com.github.fabriciofx.cactoos.jdbc.statement.StatementSelect;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.cactoos.Scalar;
import org.cactoos.iterator.Mapped;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.FormattedText;
import org.cactoos.text.Joined;

/**
 * Phones for SQL.
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
        "PMD.AvoidThrowingRawExceptionTypes"
    }
)
public final class PhonesSql implements Phones {
    /**
     * Session.
     */
    private final Session session;

    /**
     * Contact's ID.
     */
    private final UUID id;

    /**
     * Ctor.
     * @param sssn A Session
     * @param contact A Contact's ID
     */
    public PhonesSql(final Session sssn, final UUID contact) {
        this.session = sssn;
        this.id = contact;
    }

    @Override
    public int count() throws Exception {
        return new ResultSetAsValue<Integer>(
            new StatementSelect(
                this.session,
                new QuerySimple(
                    new Joined(
                        " ",
                        "SELECT COUNT(number) FROM phone WHERE",
                        "contact_id = :contact_id"
                    ),
                    new ParamUuid("contact_id", this.id)
                )
            )
        ).value();
    }

    @Override
    public Phone get(final int index) throws Exception {
        final Scalar<String> number = new ResultSetAsValue<>(
            new StatementSelect(
                this.session,
                new QuerySimple(
                    new FormattedText(
                        new Joined(
                            " ",
                            "SELECT number FROM phone WHERE",
                            "contact_id = :contact_id",
                            "FETCH FIRST %d ROWS ONLY"
                        ),
                        index
                    )
                )
            )
        );
        return new PhoneSql(this.session, this.id, number.value());
    }

    @Override
    public void add(final Map<String, String> properties) throws Exception {
        new StatementInsert(
            this.session,
            new QuerySimple(
                new Joined(
                    " ",
                    "INSERT INTO phone (contact_id, number, carrier)",
                    "VALUES (:contact_id, :number, :carrier)"
                ),
                new ParamUuid("contact_id", this.id),
                new ParamText("number", properties.get("number")),
                new ParamText("carrier", properties.get("carrier"))
            )
        ).result();
    }

    @Override
    public Iterator<Phone> iterator() {
        final Unchecked<List<String>> numbers = new Unchecked<>(
            new ResultSetAsValues<>(
                new StatementSelect(
                    this.session,
                    new QuerySimple(
                        new Joined(
                            " ",
                            "SELECT number FROM phone WHERE",
                            "contact_id = :contact_id"
                        ),
                        new ParamUuid("contact_id", this.id)
                    )
                )
            )
        );
        return new Mapped<>(
            number -> new PhoneSql(this.session, this.id, number),
            numbers.value().iterator()
        );
    }
}
