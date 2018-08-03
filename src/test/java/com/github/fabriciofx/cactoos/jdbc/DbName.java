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
package com.github.fabriciofx.cactoos.jdbc;

import java.util.Random;
import org.cactoos.Text;

public final class DbName implements Text {
    private final String lexicon;
    private final Random random;
    private final int length;

    public DbName() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 5);
    }

    public DbName(final String lex, final int len) {
        this.lexicon = lex;
        this.length = len;
        this.random = new Random();
    }

    @Override
    public String asString() throws Exception {
        final StringBuilder strb = new StringBuilder(this.length);
        for (int len = 0; len < this.length; len++) {
            strb.append(
                this.lexicon.charAt(
                    this.random.nextInt(
                        this.lexicon.length()
                    )
                )
            );
        }
        return strb.toString();
    }
}
