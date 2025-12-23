/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook;

import com.github.fabriciofx.cactoos.jdbc.Pagination;
import java.util.List;

/**
 * Phonebook.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.4
 */
public interface Phonebook extends Pagination<Contact> {
    /**
     * Create a new {@link Contact}.
     * @param name Contact's name
     * @return A new {@link Contact}
     * @throws Exception Is something goes wrong
     */
    Contact create(String name) throws Exception;

    /**
     * Search for a {@link Contact}.
     * @param name Contact's name
     * @return A list of contacts
     * @throws Exception Is something goes wrong
     */
    List<Contact> search(String name) throws Exception;
}
