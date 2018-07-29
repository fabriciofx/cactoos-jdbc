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
package com.github.fabriciofx.cactoos.jdbc.agenda;

/**
 * Phone.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 */
public interface Phone {
    /**
     * Retrieve contact's phone number.
     * @return The number
     * @throws Exception If fails
     */
    String number() throws Exception;

    /**
     * Retrieve contact's phone carrier.
     * @return The carrier's name
     * @throws Exception If fails
     */
    String carrier() throws Exception;

    /**
     * Delete a contact's phone.
     * @throws Exception If fails
     */
    void delete() throws Exception;

    /**
     * Change the contact's number and carrier.
     * @param number The new number
     * @param carrier The new carrier
     * @throws Exception If fails
     */
    void change(String number, String carrier) throws Exception;
}
