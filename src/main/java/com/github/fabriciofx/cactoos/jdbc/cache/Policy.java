/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import java.util.List;
import org.cactoos.Func;

/**
 * Policy.
 * @param <V> Value type which policy will be applied.
 * @param <E> Element type which will be removed by the policy.
 * @since 0.9.0
 */
public interface Policy<V, E> extends Func<V, List<E>> {
}
