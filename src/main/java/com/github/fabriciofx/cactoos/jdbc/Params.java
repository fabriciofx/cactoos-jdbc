/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018-2022 Fabrício Barros Cabral
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

/**
 * Query Params.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 */
public interface Params extends Iterable<Param> {
    /**
     * Set the PreparedStatement with all query parameters.
     * @param stmt The PreparedStatement
     * @return The setted PreparedStatement
     * @throws Exception If fails
     */
    PreparedStatement prepare(PreparedStatement stmt) throws Exception;

    /**
     * Check if Params contains a param at position.
     * @param name The name of the parameter
     * @param index The position of the parameter in the PreparedStatement
     * @return True if contains or false if don't
     */
    boolean contains(String name, int index);
}
