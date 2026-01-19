/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import java.util.List;
import java.util.Map;
import org.cactoos.Func;

/**
 * Policy.
 * @param <D> The domain key type
 * @param <V> The value type stored
 * @since 0.9.0
 */
public interface Policy<D, V>
    extends Func<Map<Key<D>, Entry<D, V>>, List<Entry<D, V>>> {
}
