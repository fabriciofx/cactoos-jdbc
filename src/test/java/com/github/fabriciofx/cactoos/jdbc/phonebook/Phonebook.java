/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook;

import com.github.fabriciofx.cactoos.jdbc.pagination.Pages;

/**
 * Phonebook.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.4
 */
public interface Phonebook {
    /**
     * Create a new Contact.
     * @param name Contact's name
     * @return A new Contact
     * @throws Exception If fails
     */
    Contact contact(String name) throws Exception;

    /**
     * Search for a Contact.
     * @param name Contact's name
     * @return A Contact's pages
     * @throws Exception If fails
     */
    Pages<Contact> search(String name) throws Exception;

    /**
     * Retrieve all Contacts.
     * @param max The max amount contacts per page
     * @return Pages with all phonebook Contacts
     * @throws Exception Is fails
     */
    Pages<Contact> contacts(int max) throws Exception;
}
