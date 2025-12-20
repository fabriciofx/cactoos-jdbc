/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook;

/**
 * Contacts.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 */
public interface Contacts extends Iterable<Contact> {
    /**
     * Returns the quantity of contacts.
     * @return The quantity
     * @throws Exception If fails
     */
    int count() throws Exception;

    /**
     * Returns the contact in the set.
     * @param index The contact (element) position in the set
     * @return The found contact
     * @throws Exception If fails
     */
    Contact get(int index) throws Exception;
}
