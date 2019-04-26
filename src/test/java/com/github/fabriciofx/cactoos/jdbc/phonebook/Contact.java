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
package com.github.fabriciofx.cactoos.jdbc.phonebook;

import java.util.Map;
import java.util.UUID;

/**
 * Contact.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 *
 * @checkstyle MethodNameCheck (100 lines)
 */
@SuppressWarnings("PMD.ShortMethodName")
public interface Contact {
    /**
     * Retrieve the Contact's ID.
     * @return Contact's ID
     */
    UUID id();

    /**
     * Retrieve the Contact's data.
     * @return Contact's data
     * @throws Exception If fails
     */
    String about() throws Exception;

    /**
     * Retrieve the Contact's phones.
     * @return Phones
     * @throws Exception If fails
     */
    Phones phones() throws Exception;

    /**
     * Delete a Contact.
     * @throws Exception I fails
     */
    void delete() throws Exception;

    /**
     * Update a Contact.
     * @param properties New properties
     * @throws Exception If fails
     */
    void update(Map<String, String> properties) throws Exception;
}
