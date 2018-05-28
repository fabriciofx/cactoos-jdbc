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
package com.github.fabriciofx.cactoos.jdbc.result;

import com.github.fabriciofx.cactoos.jdbc.DataStream;
import com.github.fabriciofx.cactoos.jdbc.Result;
import com.github.fabriciofx.cactoos.jdbc.stream.XmlDataStream;
import java.util.List;
import java.util.Map;
import org.cactoos.Scalar;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class ResultAsXml implements Scalar<DataStream> {
    private final Result<List<Map<String, Object>>> statements;
    private final DataStream stream;
    private final String child;

    public ResultAsXml(
        final Result<List<Map<String, Object>>> stmts,
        final String root,
        final String chld
    ) {
        this.statements = stmts;
        this.stream = new XmlDataStream(root);
        this.child = chld;
    }

    @Override
    public DataStream value() throws Exception {
        for (final Map<String, Object> fields : this.statements.value()) {
            final DataStream substream = this.stream.substream(this.child);
            for (final String name : fields.keySet()) {
                substream.with(name, () -> fields.get(name).toString());
            }
            this.stream.add(substream);
        }
        return this.stream;
    }
}
