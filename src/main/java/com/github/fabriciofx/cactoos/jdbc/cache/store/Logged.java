/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache.store;

import com.github.fabriciofx.cactoos.jdbc.cache.Store;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Ternary;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.FormattedText;
import org.cactoos.text.UncheckedText;

/**
 * Logged.
 * <p>
 * A {@link Store} decorator to logging store operations.
 *
 * @param <K> The key type
 * @param <V> The value type
 * @since 0.9.0
 * @checkstyle ParameterNumberCheck (500 lines)
 */
public final class Logged<K, V> implements Store<K, V> {
    /**
     * Cache to be logged.
     */
    private final Store<K, V> origin;

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
     * @param origin The cache to be logged
     * @param from Where the data comes from
     */
    public Logged(final Store<K, V> origin, final String from) {
        this(origin, from, Logger.getLogger(from));
    }

    /**
     * Ctor.
     *
     * @param origin The cache to be logged
     * @param from Where the data comes from
     * @param logger The logger
     */
    public Logged(
        final Store<K, V> origin,
        final String from,
        final Logger logger
    ) {
        this(
            origin,
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
     * @param cache The cache to be logged
     * @param from Where the data comes from
     * @param logger The logger
     * @param level The logger level
     */
    public Logged(
        final Store<K, V> cache,
        final String from,
        final Logger logger,
        final Unchecked<Level> level
    ) {
        this.origin = cache;
        this.from = from;
        this.logger = logger;
        this.level = level;
    }

    @Override
    public V retrieve(final K key) {
        final V value = this.origin.retrieve(key);
        this.logger.log(
            this.level.value(),
            new UncheckedText(
                new FormattedText(
                    "[%s] Retrieving from cache with key '%s' and value '%s'.",
                    this.from,
                    key.toString(),
                    value.toString()
                )
            ).asString()
        );
        return value;
    }

    @Override
    public List<V> save(final K key, final V value) throws Exception {
        final List<V> removed = this.origin.save(key, value);
        this.logger.log(
            this.level.value(),
            new UncheckedText(
                new FormattedText(
                    "[%s] Storing in cache with key '%s' and value '%s'.",
                    this.from,
                    key.toString(),
                    value.toString()
                )
            ).asString()
        );
        return removed;
    }

    @Override
    public V delete(final K key) {
        final V value = this.origin.delete(key);
        this.logger.log(
            this.level.value(),
            new UncheckedText(
                new FormattedText(
                    "[%s] Deleting into cache with key '%s' and returning value '%s'.",
                    this.from,
                    key.toString(),
                    new Unchecked<>(
                        new Ternary<>(
                            value != null,
                            () -> value.toString(),
                            () -> "(null)"
                        )
                    ).value()
                )
            ).asString()
        );
        return value;
    }

    @Override
    public boolean contains(final K key) {
        final boolean exists = this.origin.contains(key);
        this.logger.log(
            this.level.value(),
            new UncheckedText(
                new FormattedText(
                    "[%s] Checking if cache has a value for key '%s': %s.",
                    this.from,
                    key.toString(),
                    exists
                )
            ).asString()
        );
        return exists;
    }

    @Override
    public void clear() {
        this.origin.clear();
        this.logger.log(
            this.level.value(),
            new UncheckedText(
                new FormattedText(
                    "[%s] Cleaning the cache and resetting statistics.",
                    this.from
                )
            ).asString()
        );
    }
}
