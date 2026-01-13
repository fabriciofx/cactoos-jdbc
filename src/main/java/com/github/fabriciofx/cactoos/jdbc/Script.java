/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

/**
 * Script.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.2
 */
@FunctionalInterface
public interface Script {
    /**
     * Execute this Script on the source.
     * @param source The source
     * @throws Exception if fails
     */
    void run(Source source) throws Exception;
}
