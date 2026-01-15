package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import com.github.fabriciofx.cactoos.jdbc.Store;
import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.cache.statistics.StatisticsOf;
import com.github.fabriciofx.cactoos.jdbc.cache.store.TableStore;
import org.cactoos.Scalar;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;

public final class TableCache implements Cache<String, Table> {
    private final Scalar<Store<String, Table>> scalar;

    public TableCache() {
        this(new Sticky<>(TableStore::new));
    }

    public TableCache(final Scalar<Store<String, Table>> scalar) {
        this.scalar = scalar;
    }

    @Override
    public Store<String, Table> store() {
        return new Unchecked<>(this.scalar).value();
    }

    @Override
    public Statistics statistics() {
        return new StatisticsOf();
    }
}
