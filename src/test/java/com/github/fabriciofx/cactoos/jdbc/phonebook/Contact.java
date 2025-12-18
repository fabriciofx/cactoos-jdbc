/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook;

/**
 * Contact.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 *
 * @checkstyle MethodNameCheck (100 lines)
 */
public interface Contact {
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
     * @param name New contact's name
     * @throws Exception If fails
     */
    void update(String name) throws Exception;
}
