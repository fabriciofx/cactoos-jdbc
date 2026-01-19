/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache.store;

import com.github.fabriciofx.cactoos.jdbc.cache.Entry;
import com.github.fabriciofx.cactoos.jdbc.cache.Key;
import com.github.fabriciofx.cactoos.jdbc.cache.Store;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cactoos.Text;
import org.cactoos.iterable.Mapped;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Ternary;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.FormattedText;
import org.cactoos.text.Joined;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;

/**
 * Logged Store.
 * <p>A {@link Store} decorator to logging store operations.
 * @param <D> The domain key type
 * @param <V> The value type stored
 * @since 0.9.0
 * @checkstyle ParameterNumberCheck (500 lines)
 */
public final class Logged<D, V> implements Store<D, V> {
    /**
     * Store.
     */
    private final Store<D, V> origin;

    /**
     * Where the logs come from.
     */
    private final String from;

    /**
     * Logger.
     */
    private final Logger logger;

    /**
     * Level log.
     */
    private final Unchecked<Level> level;

    /**
     * Ctor.
     *
     * @param store The cache to be logged
     * @param from Where the data comes from
     */
    public Logged(final Store<D, V> store, final String from) {
        this(store, from, Logger.getLogger(from));
    }

    /**
     * Ctor.
     *
     * @param store The cache to be logged
     * @param from Where the data comes from
     * @param logger The logger
     */
    public Logged(
        final Store<D, V> store,
        final String from,
        final Logger logger
    ) {
        this(
            store,
            from,
            logger,
            new Unchecked<>(
                new Sticky<>(
                    () -> {
                        Level lvl = logger.getLevel();
                        if (lvl == null) {
                            Logger parent = logger;
                            while (lvl == null) {
                                parent = parent.getParent();
                                lvl = parent.getLevel();
                            }
                        }
                        return lvl;
                    }
                )
            )
        );
    }

    /**
     * Ctor.
     *
     * @param store The cache to be logged
     * @param from Where the data comes from
     * @param logger The logger
     * @param level The logger level
     */
    public Logged(
        final Store<D, V> store,
        final String from,
        final Logger logger,
        final Unchecked<Level> level
    ) {
        this.origin = store;
        this.from = from;
        this.logger = logger;
        this.level = level;
    }

    @Override
    public Entry<D, V> retrieve(final Key<D> key) {
        final Entry<D, V> entry = this.origin.retrieve(key);
        this.logger.log(
            this.level.value(),
            new UncheckedText(
                new FormattedText(
                    "[%s] Retrieving from cache with key '%s' and value '%s'.",
                    this.from,
                    key.hash(),
                    entry.value().toString()
                )
            ).asString()
        );
        return entry;
    }

    @Override
    public List<Entry<D, V>> save(
        final Key<D> key,
        final Entry<D, V> entry
    ) throws Exception {
        final List<Entry<D, V>> removed = this.origin.save(key, entry);
        this.logger.log(
            this.level.value(),
            new UncheckedText(
                new FormattedText(
                    "[%s] Storing in cache with key '%s' and value '%s'.",
                    this.from,
                    key.hash(),
                    entry.value().toString()
                )
            ).asString()
        );
        return removed;
    }

    @Override
    public Entry<D, V> delete(final Key<D> key) {
        final Entry<D, V> entry = this.origin.delete(key);
        this.logger.log(
            this.level.value(),
            new UncheckedText(
                new FormattedText(
                    "[%s] Deleting into cache with key '%s' and returning value '%s'.",
                    this.from,
                    key.hash(),
                    new Unchecked<>(
                        new Ternary<>(
                            entry != null,
                            () -> entry.value().toString(),
                            () -> "(null)"
                        )
                    ).value()
                )
            ).asString()
        );
        return entry;
    }

    @Override
    public boolean contains(final Key<D> key) {
        final boolean exists = this.origin.contains(key);
        this.logger.log(
            this.level.value(),
            new UncheckedText(
                new FormattedText(
                    "[%s] Checking if cache has a value for key '%s': %s.",
                    this.from,
                    key.hash(),
                    exists
                )
            ).asString()
        );
        return exists;
    }

    @Override
    public List<Entry<D, V>> invalidate(final Iterable<String> metadata) {
        final List<Entry<D, V>> removed = this.origin.invalidate(metadata);
        this.logger.log(
            this.level.value(),
            new UncheckedText(
                new FormattedText(
                    "[%s] Invalidating %d cache entries with keys: '%s'",
                    this.from,
                    removed.size(),
                    new Joined(
                        new TextOf(", "),
                        new Mapped<Text>(
                            entry -> new TextOf(entry.key().hash()),
                            removed
                        )
                    )
                )
            ).asString()
        );
        return removed;
    }

    @Override
    public void clear() {
        this.origin.clear();
        this.logger.log(
            this.level.value(),
            new UncheckedText(
                new FormattedText(
                    "[%s] Cleaning the cache.",
                    this.from
                )
            ).asString()
        );
    }
}
