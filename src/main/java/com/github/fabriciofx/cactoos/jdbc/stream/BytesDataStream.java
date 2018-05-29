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
import java.nio.charset.StandardCharsets;
import org.cactoos.Bytes;
import org.cactoos.Output;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version Id
 * @since
 */
public final class BytesDataStream implements DataStream {
    private final Bytes bytes;

    public BytesDataStream(final byte[] bts) {
        this(() -> bts);
    }

    public BytesDataStream(final Bytes bts) {
        this.bytes = bts;
    }

    @Override
    public void save(final Output output) throws Exception {
        output.stream().write(this.bytes.asBytes());
    }

    @Override
    public String asString() throws Exception {
        return new String(this.bytes.asBytes(), StandardCharsets.UTF_8);
    }
}
