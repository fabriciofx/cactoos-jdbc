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
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class DecimalParam implements DataParam {
    private final String name;
    private final BigDecimal value;

    public DecimalParam(final String name, final String value) {
        this(name, new BigDecimal(value));
    }

    public DecimalParam(final String name, final BigDecimal value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int count() {
        return 1;
    }

    @Override
    public void prepare(
        final int pos,
        final PreparedStatement stmt
    ) throws SQLException {
        stmt.setBigDecimal(pos, this.value);
    }

    @Override
    public DataStream stream(final DataStream stream) throws Exception {
        return stream.with(this.name, this);
    }

    @Override
    public String asString() throws Exception {
        return this.value.toString();
    }
}
