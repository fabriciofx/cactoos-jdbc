/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Fabricio Barros Cabral
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
package com.github.fabriciofx.cactoos.jdbc;

import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.List;
import org.cactoos.list.ListOf;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;

/**
 * Query Params.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 */
public final class Params implements Iterable<Param> {
    /**
     * Params.
     */
    private final Unchecked<List<Param>> prms;

    /**
     * Ctor.
     * @param prms List of Param
     */
    public Params(final Param... prms) {
        this.prms = new Unchecked<>(
            new Sticky<>(
                () -> new ListOf<>(prms)
            )
        );
    }

    /**
     * Set the PreparedStatement with all query prms.
     * @param stmt The PreparedStatement
     * @return The setted PreparedStatement
     * @throws Exception If fails
     */
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

    /**
     * Check if Params contains a param at position.
     * @param name The name of the parameter
     * @param pos The position of the parameter
     * @return True if contains or false if don't
     */
    public boolean contains(final String name, final int pos) {
        return this.prms.value().get(pos).name().equals(name);
    }

    /**
     * Return an iterator over Params.
     * @return The iterator
     */
    public Iterator<Param> iterator() {
        return this.prms.value().iterator();
    }
}
