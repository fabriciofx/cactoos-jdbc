/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook;

/**
 * Phones.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 */
public interface Phones extends Iterable<Phone> {
    /**
     * Returns the quantity of contact's phones.
     * @return The quantity
     * @throws Exception If fails
     */
    int count() throws Exception;

    /**
     * Returns the phone in the set.
     * @param index The phone (element) position in the set
     * @return The found phone
     * @throws Exception If fails
     */
    Phone get(int index) throws Exception;

    /**
     * Add a new phone to contact's phones.
     * @param number The phone's number
     * @param carrier The phone's carrier
     * @throws Exception If fails
     */
    void add(String number, String carrier) throws Exception;
}
