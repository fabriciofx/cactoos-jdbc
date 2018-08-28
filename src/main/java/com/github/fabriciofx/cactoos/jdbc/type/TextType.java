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
package com.github.fabriciofx.cactoos.jdbc.type;

import com.github.fabriciofx.cactoos.jdbc.DataType;
import java.sql.ResultSet;
import java.sql.Types;

/**
 * String type.
 *
 * @since 0.2
 */
public final class TextType implements DataType<String> {
    /**
     * Ctor.
     */
    public TextType() {
    }

    @Override
    public boolean match(final int type) {
        return type == Types.VARCHAR ||
            type == Types.LONGVARCHAR ||
            type == Types.CHAR ||
            type == Types.NVARCHAR ||
            type == Types.NCHAR ||
            type == Types.LONGNVARCHAR;
    }

    @Override
    public String data(
        final ResultSet rset,
        final int index
    ) throws Exception {
        return rset.getString(index);
    }
}
