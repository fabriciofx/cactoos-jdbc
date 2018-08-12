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
package com.github.fabriciofx.cactoos.jdbc.script;

import com.github.fabriciofx.cactoos.jdbc.Server;
import com.github.fabriciofx.cactoos.jdbc.server.H2Server;
import com.github.fabriciofx.cactoos.jdbc.server.MySqlServer;
import org.cactoos.io.ResourceOf;
import org.junit.Test;

public final class SqlScriptTest {
    @Test
    public void h2() throws Exception {
        final Server h2 = new H2Server(
            new SqlScriptFromInput(
                new ResourceOf(
                    "com/github/fabriciofx/cactoos/jdbc/agenda/agendadb-h2.sql"
                )
            )
        );
        h2.start();
        h2.stop();
    }

    @Test
    public void mysql() throws Exception {
        final Server mysql = new MySqlServer(
            new OldSqlScriptFromInput(
                new ResourceOf(
                    "com/github/fabriciofx/cactoos/jdbc/agenda/agendadb-mysql.sql"
                )
            )
        );
        mysql.start();
        mysql.stop();
    }
}
