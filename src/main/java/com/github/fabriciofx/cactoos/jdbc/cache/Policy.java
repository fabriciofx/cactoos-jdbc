package com.github.fabriciofx.cactoos.jdbc.cache;

public interface Policy<V> {
    void apply(V value);
}
