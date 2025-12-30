package com.github.fabriciofx.cactoos.jdbc.cache;

import java.sql.ResultSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ResultSetCache implements Cache<String, ResultSet> {
    private final Map<String, ResultSet> results;

    public ResultSetCache() {
        this(new ConcurrentHashMap<>());
    }

    public ResultSetCache(final Map<String, ResultSet> results) {
        this.results = results;
    }

    @Override
    public ResultSet retrieve(final String key) {
        return this.results.get(key);
    }

    @Override
    public void store(final String key, final ResultSet value) {
        this.results.put(key, value);
    }

    @Override
    public boolean contains(final String key) {
        return this.results.containsKey(key);
    }

    @Override
    public void clear() {
        this.results.clear();
    }
}
