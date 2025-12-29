package com.github.fabriciofx.cactoos.jdbc.cache;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.FormattedText;
import org.cactoos.text.UncheckedText;

public final class Logged<K, V> implements Cache<K, V> {
    private final Cache<K, V> origin;
    private final Logger logger;
    private final Unchecked<Level> level;

    public Logged(final Cache<K, V> origin, final Logger logger) {
        this.origin = origin;
        this.logger = logger;
        this.level = new Unchecked<>(
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
        );
    }

    @Override
    public V retrieve(final K key) {
        final V value = this.origin.retrieve(key);
        this.logger.log(
            this.level.value(),
            new UncheckedText(
                new FormattedText(
                    "Retrieving from cache with key '%s' and value '%s'.",
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
                    "Storing in cache with key '%s' and value '%s'.",
                    key.toString(),
                    value.toString()
                )
            ).asString()
        );
    }

    @Override
    public boolean contains(final K key) {
        final boolean exists = this.origin.contains(key);
        this.logger.log(
            this.level.value(),
            new UncheckedText(
                new FormattedText(
                    "Checking if cache has a value for key '%s': %s.",
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
            new UncheckedText("Cleaning the cache.").asString()
        );
    }
}
