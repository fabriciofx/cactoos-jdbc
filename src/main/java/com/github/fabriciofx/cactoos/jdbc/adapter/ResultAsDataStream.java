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
package com.github.fabriciofx.cactoos.jdbc.adapter;

import com.github.fabriciofx.cactoos.jdbc.DataStream;
import com.github.fabriciofx.cactoos.jdbc.Statements;
import com.github.fabriciofx.cactoos.jdbc.stream.XmlDataStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Map;
import org.cactoos.Func;
import org.cactoos.Scalar;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class ResultAsDataStream implements Scalar<DataStream> {
    private final Statements<List<Map<String, Object>>> statements;
    private final DataStream stream;

    public ResultAsDataStream(
        final Statements<List<Map<String, Object>>> stmts,
        final DataStream strm
    ) {
        this.statements = stmts;
        this.stream = strm;
    }

    @Override
    public DataStream value() throws Exception {
        for (final Map<String, Object> fields : this.statements.value()) {
            for (final String name : fields.keySet()) {
                this.stream.with(name, () -> fields.get(name).toString());
            }
        }
        return this.stream;
    }
}
