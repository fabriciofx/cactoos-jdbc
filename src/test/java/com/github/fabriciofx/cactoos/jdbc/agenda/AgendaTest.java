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

import com.github.fabriciofx.cactoos.jdbc.H2Source;
import com.github.fabriciofx.cactoos.jdbc.Result;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.adapter.ResultSetToStream;
import com.github.fabriciofx.cactoos.jdbc.script.ScriptSql;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuthSession;
import com.github.fabriciofx.cactoos.jdbc.stmt.Select;
import org.cactoos.io.ResourceOf;
import org.junit.Test;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class AgendaTest {
    @Test
    public void agenda() throws Exception {
        final Session session = new NoAuthSession(
            new H2Source("agendadb")
        );
        new ScriptSql(
            session,
            new ResourceOf(
                "com/github/fabriciofx/cactoos/jdbc/agenda/agendadb.sql"
            )
        ).exec();
        final Contacts contacts = new SqlContacts(session);
        final Contact joseph = contacts.contact("Joseph Klimber");
        joseph.phones().phone("99991234", "TIM");
        joseph.phones().phone("98812564", "Oi");
        System.out.println(joseph.asString());

        final Contact maria = contacts.find("maria");
        System.out.println(maria.asString());
    }
}
