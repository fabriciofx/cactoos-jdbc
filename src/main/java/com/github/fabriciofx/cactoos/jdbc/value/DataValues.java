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
package com.github.fabriciofx.cactoos.jdbc.value;

import com.github.fabriciofx.cactoos.jdbc.DataValue;
import com.github.fabriciofx.cactoos.jdbc.DataStream;
import com.github.fabriciofx.cactoos.jdbc.stream.XmlDataStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class DataValues implements DataValue, Iterable<DataValue> {
    private final List<DataValue> params;
    private final DataStream stream;

    public DataValues(final DataValue... prms) {
        this("params", prms);
    }

    public DataValues(final String name, final DataValue... prms) {
        this(new XmlDataStream("", name), prms);
    }

    public DataValues(final DataStream strm, final DataValue... prms) {
        this.stream = strm;
        this.params = new ArrayList<>();
        for (final DataValue p : prms) {
            this.params.add(p);
        }
    }

    public DataValue get(final int index) {
        return this.params.get(index);
    }

    public DataValues with(final DataValue param) {
        this.params.add(param);
        return this;
    }

    @Override
    public void prepare(
        final int pos,
        final PreparedStatement stmt
    ) throws SQLException {
        int idx = pos;
        for (final DataValue param : this.params) {
            param.prepare(idx, stmt);
            ++idx;
        }
    }

    @Override
    public DataStream stream(final DataStream stream) throws Exception {
        for (final DataValue param : this.params) {
            param.stream(stream);
        }
        return stream;
    }

    @Override
    public Iterator<DataValue> iterator() {
        return this.params.iterator();
    }

    @Override
    public String asString() throws Exception {
        return this.stream(this.stream).asString();
    }
}
