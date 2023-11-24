/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018-2023 Fabrício Barros Cabral
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.fabriciofx.cactoos.jdbc.params;

import com.github.fabriciofx.cactoos.jdbc.Param;
import com.github.fabriciofx.cactoos.jdbc.Params;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.List;
import org.cactoos.list.Joined;
import org.cactoos.list.ListOf;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;

/**
 * Named Query Params.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.4
 */
public final class ParamsOf implements Params {
    /**
     * Params.
     */
    private final Unchecked<List<Param>> prms;

    /**
     * Ctor.
     * @param params Params
     * @param prms Array of Param
     */
    public ParamsOf(final Params params, final Param... prms) {
        this(
            new Joined<Param>(
                new ListOf<>(params.iterator()),
                new ListOf<>(prms)
            )
        );
    }

    /**
     * Ctor.
     * @param prms Array of Param
     */
    public ParamsOf(final Param... prms) {
        this(new ListOf<>(prms));
    }

    /**
     * Ctor.
     * @param prms List of Param
     */
    public ParamsOf(final List<Param> prms) {
        this.prms = new Unchecked<>(
            new Sticky<>(
                () -> prms
            )
        );
    }

    @Override
    public PreparedStatement prepare(
        final PreparedStatement stmt
    ) throws Exception {
        int idx = 1;
        for (final Param param : this.prms.value()) {
            param.prepare(stmt, idx);
            ++idx;
        }
        return stmt;
    }

    @Override
    public boolean contains(final String name, final int index) {
        return this.prms.value().get(index).name().equals(name);
    }

    @Override
    public Iterator<Param> iterator() {
        return this.prms.value().iterator();
    }
}
