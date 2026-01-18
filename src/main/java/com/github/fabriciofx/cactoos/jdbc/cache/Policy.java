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
 * @since 0.9.0
 */
public interface Policy extends Func<Map<Key, Entry>, List<Entry>> {
}
