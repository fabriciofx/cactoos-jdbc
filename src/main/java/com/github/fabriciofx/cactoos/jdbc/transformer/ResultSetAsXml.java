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
package com.github.fabriciofx.cactoos.jdbc.transformer;

import com.github.fabriciofx.cactoos.jdbc.DataStream;
import com.github.fabriciofx.cactoos.jdbc.Transformer;
import com.github.fabriciofx.cactoos.jdbc.stream.BytesDataStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version Id
 * @since
 */
public final class ResultSetAsXml implements Transformer {
    private final String root;
    private final String child;

    public ResultSetAsXml(final String root, final String child) {
        this.root = root;
        this.child = child;
    }

    @Override
    public DataStream transform(final ResultSet rset) throws Exception {
        final StringBuilder strb = new StringBuilder();
        strb.append(String.format("<%s>", this.root));
        while (rset.next()) {
            final ResultSetMetaData rsmd = rset.getMetaData();
            final int cols = rsmd.getColumnCount();
            strb.append(String.format("<%s>", this.child));
            for (int i = 1; i <= cols; i++) {
                strb.append(
                    String.format(
                        "<%s>%s</%s>",
                        rsmd.getColumnName(i).toLowerCase(),
                        rset.getObject(i).toString(),
                        rsmd.getColumnName(i).toLowerCase()
                    )
                );
            }
            strb.append(String.format("</%s>", this.child));
        }
        strb.append(String.format("</%s>", this.root));
        return new BytesDataStream(strb.toString().getBytes());
    }
}
