/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.FormattedText;
import org.cactoos.text.UncheckedText;

/**
 * Logged.
 * A {@link Cache} decorator to logging caching operations.
 * @param <K> The key type
 * @param <V> The value type
 * @since 0.9.0
 * @checkstyle ParameterNumberCheck (500 lines)
 */
public final class Logged<K, V> implements Cache<K, V> {
    /**
     * Cache to be logged.
     */
    private final Cache<K, V> origin;

    /**
     * Where the data comes from.
     */
    private final String source;

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
     * @param origin The cache to be logged
     * @param source Where the data comes from
     */
    public Logged(final Cache<K, V> origin, final String source) {
        this(origin, source, Logger.getLogger(source));
    }

    /**
     * Ctor.
     * @param origin The cache to be logged
     * @param source Where the data comes from
     * @param logger The logger
     */
    public Logged(
        final Cache<K, V> origin,
        final String source,
        final Logger logger
    ) {
        this(
            origin,
            source,
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
     * @param origin The cache to be logged
     * @param source Where the data comes from
     * @param logger The logger
     * @param level The logger level
     */
    public Logged(
        final Cache<K, V> origin,
        final String source,
        final Logger logger,
        final Unchecked<Level> level
    ) {
        this.origin = origin;
        this.source = source;
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
                    this.source,
                    key.toString(),
                    value.toString()
                )
            ).asString()
        );
        return value;
    }

    @Override
    public void store(final K key, final V value) {
        this.origin.store(key, value);
        this.logger.log(
            this.level.value(),
            new UncheckedText(
                new FormattedText(
                    "[%s] Storing in cache with key '%s' and value '%s'.",
                    this.source,
                    key.toString(),
                    value.toString()
                )
            ).asString()
        );
    }

    @Override
    public V delete(final K key) {
        final V value = this.origin.delete(key);
        this.logger.log(
            this.level.value(),
            new UncheckedText(
                new FormattedText(
                    "[%s] Deleting into cache with key '%s' and returning value '%s'.",
                    this.source,
                    key.toString(),
                    value.toString()
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
                    this.source,
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
                    "[%s] Cleaning the cache.",
                    this.source
                )
            ).asString()
        );
    }
}
