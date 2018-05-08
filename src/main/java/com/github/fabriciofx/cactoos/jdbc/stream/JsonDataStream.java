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
package com.github.fabriciofx.cactoos.jdbc.stream;

import com.github.fabriciofx.cactoos.jdbc.DataStream;
import org.cactoos.Output;
import org.cactoos.Text;
import org.json.XML;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class JsonDataStream implements DataStream {
    private final DataStream xml;

    public JsonDataStream() {
        this("json");
    }

    public JsonDataStream(final String name) {
        this.xml = new XmlDataStream(name);
    }

    public JsonDataStream(final DataStream strm) {
        this.xml = strm;
    }

    @Override
    public DataStream with(
        final String name,
        final Text value
    ) throws Exception {
        this.xml.with(name, value);
        return this;
    }

    @Override
    public void save(final Output output) throws Exception {
        this.xml.save(output);
    }

    @Override
    public String asString() throws Exception {
        return XML.toJSONObject(this.xml.asString()).toString();
    }
}
