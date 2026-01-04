/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.script;

import com.github.fabriciofx.cactoos.jdbc.Source;
import org.cactoos.io.DeadInput;

/**
 * An empty SQL Script.
 *
 * @since 0.4
 */
public final class EmptyScript extends ScriptOf {
    /**
     * Ctor.
     */
    public EmptyScript() {
        super(new DeadInput());
    }

    @Override
    public void run(final Source source) throws Exception {
        // Intended empty.
    }
}
