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
package com.github.fabriciofx.cactoos.jdbc.param;

import com.github.fabriciofx.cactoos.jdbc.DataParam;
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
public final class DataParams implements DataParam, Iterable<DataParam> {
    private final List<DataParam> params;
    private final DataStream stream;

    public DataParams(final DataParam... prms) {
        this("params", prms);
    }

    public DataParams(final String name, final DataParam... prms) {
        this(new XmlDataStream("", name), prms);
    }

    public DataParams(final DataStream strm, final DataParam... prms) {
        this.stream = strm;
        this.params = new ArrayList<>();
        for (final DataParam p : prms) {
            this.params.add(p);
        }
    }

    public DataParam get(final int index) {
        return this.params.get(index);
    }

    public DataParams with(final DataParam param) {
        this.params.add(param);
        return this;
    }

    @Override
    public int count() {
        return this.params.size();
    }

    @Override
    public void prepare(
        final int pos,
        final PreparedStatement stmt
    ) throws SQLException {
        int idx = pos;
        for (final DataParam param : this.params) {
            param.prepare(idx, stmt);
            ++idx;
        }
    }

    @Override
    public DataStream stream(final DataStream stream) throws Exception {
        for (final DataParam param : this.params) {
            param.stream(stream);
        }
        return stream;
    }

    @Override
    public Iterator<DataParam> iterator() {
        return this.params.iterator();
    }

    @Override
    public String asString() throws Exception {
        return this.stream(this.stream).asString();
    }
}
