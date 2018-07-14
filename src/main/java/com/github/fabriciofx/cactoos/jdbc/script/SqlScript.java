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

import com.github.fabriciofx.cactoos.jdbc.Script;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.query.NamedQuery;
import com.github.fabriciofx.cactoos.jdbc.stmt.Update;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.cactoos.Input;

/**
 * SQL Script.
 *
 * @since 0.1
 */
public final class SqlScript implements Script {
    /**
     * Session.
     */
    private final Session session;

    /**
     * Input.
     */
    private final Input input;

    /**
     * Ctor.
     * @param sssn Session where script will be executed
     * @param npt Input to be used in the session
     */
    public SqlScript(final Session sssn, final Input npt) {
        this.session = sssn;
        this.input = npt;
    }

    @Override
    public void exec() throws Exception {
        try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            final InputStream inpt = this.input.stream();
            // @checkstyle MagicNumber (1 line)
            final byte[] buf = new byte[1024];
            while (true) {
                final int length = inpt.read(buf);
                if (length == -1) {
                    break;
                }
                baos.write(buf, 0, length);
            }
            final String sql = baos.toString("UTF-8");
            new Update(
                this.session,
                new NamedQuery(sql)
            ).result();
        }
    }
}
