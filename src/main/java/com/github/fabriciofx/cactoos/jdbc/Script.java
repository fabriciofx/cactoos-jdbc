/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
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
     * Execute this Script on the session.
     * @param session The session
     * @throws Exception if fails
     */
    void run(Session session) throws Exception;
}
