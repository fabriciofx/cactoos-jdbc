/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
/*
 * SPDX-FileCopyrightText: Copyright (C) 2017-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.connection;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import org.cactoos.text.Joined;
import org.cactoos.text.UncheckedText;

/**
 * Fake handler logger.
 *
 * Borrowed from Cactoos (www.cactoos.org) project.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 */
public final class FakeHandler extends Handler {
    /**
     * Lines.
     */
    private final List<String> entries;

    /**
     * Ctor.
     */
    FakeHandler() {
        super();
        this.entries = new LinkedList<>();
    }

    @Override
    public void publish(final LogRecord record) {
        this.entries.add(record.getMessage());
    }

    @Override
    public void close() {
        // Intended empty.
    }

    @Override
    public void flush() {
        // Intended empty.
    }

    @Override
    public String toString() {
        return new UncheckedText(
            new Joined(
                System.lineSeparator(),
                this.entries
            )
        ).asString();
    }
}
