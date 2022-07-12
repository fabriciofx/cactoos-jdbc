/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018-2022 Fabrício Barros Cabral
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
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phones;
import com.github.fabriciofx.cactoos.jdbc.query.QuerySimple;
import com.github.fabriciofx.cactoos.jdbc.result.ResultSetAsValue;
import com.github.fabriciofx.cactoos.jdbc.result.ResultSetAsXmlEach;
import com.github.fabriciofx.cactoos.jdbc.statement.StatementSelect;
import com.github.fabriciofx.cactoos.jdbc.statement.StatementUpdate;
import java.util.Map;
import java.util.UUID;
import org.cactoos.text.FormattedText;
import org.cactoos.text.Joined;

/**
 * Contact for SQL.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings({"PMD.AvoidDuplicateLiterals", "PMD.ShortMethodName"})
public final class ContactSql implements Contact {
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
     * @param id A Contact's ID
     */
    public ContactSql(final Session sssn, final UUID id) {
        this.session = sssn;
        this.id = id;
    }

    @Override
    public String about() throws Exception {
        final String contact = new ResultSetAsValue<String>(
            new StatementSelect(
                this.session,
                new QuerySimple(
                    "SELECT name FROM contact WHERE id = :id",
                    new ParamUuid("id", this.id)
                )
            )
        ).value();
        final String phones = new ResultSetAsXmlEach(
            new StatementSelect(
                this.session,
                new QuerySimple(
                    new Joined(
                        " ",
                        "SELECT number, carrier FROM phone WHERE",
                        "contact_id = :contact_id"
                    ),
                    new ParamUuid("contact_id", this.id)
                )
            ),
            "phone"
        ).value();
        final String xml;
        if (phones.isEmpty()) {
            xml = "<contact><name>%s</name></contact>";
        } else {
            xml = "<contact><name>%s</name><phones>%s</phones></contact>";
        }
        return new FormattedText(
            xml,
            contact,
            phones
        ).asString();
    }

    @Override
    public Phones phones() throws Exception {
        return new PhonesSql(this.session, this.id);
    }

    @Override
    public void delete() throws Exception {
        new StatementUpdate(
            this.session,
            new QuerySimple(
                "DELETE FROM contact WHERE id = :id",
                new ParamUuid("id", this.id)
            )
        ).result();
    }

    @Override
    public void update(final Map<String, String> properties) throws Exception {
        new StatementUpdate(
            this.session,
            new QuerySimple(
                "UPDATE contact SET name = :name WHERE id = :id",
                new ParamText("name", properties.get("name")),
                new ParamUuid("id", this.id)
            )
        ).result();
    }
}
