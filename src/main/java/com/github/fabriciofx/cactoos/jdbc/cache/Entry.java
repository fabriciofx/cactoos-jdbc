/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.jdbc.Table;
import java.util.Set;

/**
 * Entry.
 * @since 0.9.0
 */
public interface Entry {
    /**
     * Retrieve the key associated with this entry.
     * @return The key
     */
    Key key();

    /**
     * Retrieve the Table associated with this entry.
     * @return The Table
     */
    Table value();

    /**
     * Retrieve the table names associated with this entry.
     * @return The table names
     */
    Set<String> tables();
}
