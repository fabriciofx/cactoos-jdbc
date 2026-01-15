package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import com.github.fabriciofx.cactoos.jdbc.Store;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cactoos.Text;
import org.cactoos.iterable.Mapped;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.FormattedText;
import org.cactoos.text.Joined;
import org.cactoos.text.Repeated;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;

public final class Logged<K,V> implements Cache<K, V> {
    private final Cache<K, V> origin;
    private final String from;
    private final Logger logger;
    private final Unchecked<Level> level;

    /**
     * Ctor.
     *
     * @param cache The cache to be logged
     * @param from Where the data comes from
     */
    public Logged(final Cache<K, V> cache, final String from) {
        this(cache, from, Logger.getLogger(from));
    }

    /**
     * Ctor.
     *
     * @param cache The cache to be logged
     * @param from Where the data comes from
     * @param logger The logger
     */
    public Logged(
        final Cache<K, V> cache,
        final String from,
        final Logger logger
    ) {
        this(
            cache,
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

    public Logged(
        final Cache<K, V> cache,
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
    public Store<K, V> store() {
        return new com.github.fabriciofx.cactoos.jdbc.cache.store.Logged<>(
            this.origin.store(),
            this.from,
            this.logger,
            this.level
        );
    }

    @Override
    public Statistics statistics() {
        final Statistics stats = this.origin.statistics();
        this.logger.log(
            this.level.value(),
            new UncheckedText(
                new FormattedText(
                    "[%s] Cache statistics:\n%s",
                    this.from,
                    new UncheckedText(
                        new Joined(
                            new TextOf("\n"),
                            new Mapped<Text>(
                                stat -> new FormattedText(
                                    "%s %s: %d",
                                    new Repeated(" ", this.from.length() + 2),
                                    stat.name(),
                                    stat.value()
                                ),
                                stats
                            )
                        )
                    ).asString()
                )
            ).asString()
        );
        return stats;
    }
}
