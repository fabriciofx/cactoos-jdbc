/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook;

/**
 * Phone.
 *
 * <p>There is no thread-safety guarantee.</p>
 *
 * @since 0.1
 */
public interface Phone {
    /**
     * Retrieve the phone's data as an XML document.
     *
     * @return Phone's data in XML format
     * @throws Exception If fails
     */
    String xml() throws Exception;

    /**
     * Delete a contact's phone.
     *
     * @throws Exception If fails
     */
    void delete() throws Exception;

    /**
     * Update the contact's phone number.
     *
     * @param number The phone number
     * @throws Exception If fails
     */
    void update(String number) throws Exception;
}
