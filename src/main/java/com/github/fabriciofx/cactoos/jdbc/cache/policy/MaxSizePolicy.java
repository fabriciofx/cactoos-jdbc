package com.github.fabriciofx.cactoos.jdbc.cache.policy;

import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.cache.Policy;
import java.util.Map;

public final class MaxSizePolicy implements Policy<Map<String, Table>> {
    private final int max;

    public MaxSizePolicy() {
        this(Integer.MAX_VALUE);
    }

    public MaxSizePolicy(final int max) {
        this.max = max;
    }

    @Override
    public void apply(final Map<String, Table> results) {
        while (results.size() > this.max) {
            results.remove(results.keySet().iterator().next());
        }
    }
}
