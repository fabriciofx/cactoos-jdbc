/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache.hash;

import org.cactoos.Bytes;

/**
 * Murmur3Hash function.
 *
 * @since 0.9.0
 * @checkstyle CyclomaticComplexityCheck (200 lines)
 * @checkstyle JavaNCSSCheck (200 lines)
 * @checkstyle ExecutableStatementCountCheck (200 lines)
 * @checkstyle FallThroughCheck (200 lines)
 * @checkstyle UnnecessaryParenthesesCheck (200 lines)
 * @checkstyle BooleanExpressionComplexityCheck (200 lines)
 */
@SuppressWarnings({
    "PMD.UselessParentheses",
    "PMD.ImplicitSwitchFallThrough",
    "PMD.NcssCount"
})
public final class Murmur3Hash implements Bytes {
    /**
     * Bytes.
     */
    private final Bytes bytes;

    /**
     * Seed.
     */
    private final int seed;

    /**
     * Ctor.
     * @param bytes Bytes
     */
    public Murmur3Hash(final Bytes bytes) {
        this(bytes, 0);
    }

    /**
     * Ctor.
     * @param bytes Bytes
     * @param seed The seed
     */
    public Murmur3Hash(final Bytes bytes, final int seed) {
        this.bytes = bytes;
        this.seed = seed;
    }

    @SuppressWarnings("fallthrough")
    @Override
    public byte[] asBytes() throws Exception {
        final byte[] data = this.bytes.asBytes();
        final int length = data.length;
        final int blocks = length >> 4;
        final long ctsa = 0x87c37b91114253d5L;
        final long ctsb = 0x4cf5ad432745937fL;
        long hasha = this.seed & 0xffffffffL;
        long hashb = this.seed & 0xffffffffL;
        for (int num = 0; num < blocks; ++num) {
            final int index = num << 4;
            long keia = littleEndian(data, index);
            long keib = littleEndian(data, index + 8);
            keia *= ctsa;
            keia = Long.rotateLeft(keia, 31);
            keia *= ctsb;
            hasha ^= keia;
            hasha = Long.rotateLeft(hasha, 27);
            hasha += hashb;
            hasha = hasha * 5 + 0x52dce729;
            keib *= ctsb;
            keib = Long.rotateLeft(keib, 33);
            keib *= ctsa;
            hashb ^= keib;
            hashb = Long.rotateLeft(hashb, 31);
            hashb += hasha;
            hashb = hashb * 5 + 0x38495ab5;
        }
        long keia = 0;
        long keib = 0;
        final int tail = blocks << 4;
        switch (length & 15) {
            case 15:
                keib ^= ((long) data[tail + 14] & 0xff) << 48;
            case 14:
                keib ^= ((long) data[tail + 13] & 0xff) << 40;
            case 13:
                keib ^= ((long) data[tail + 12] & 0xff) << 32;
            case 12:
                keib ^= ((long) data[tail + 11] & 0xff) << 24;
            case 11:
                keib ^= ((long) data[tail + 10] & 0xff) << 16;
            case 10:
                keib ^= ((long) data[tail + 9] & 0xff) << 8;
            case 9:
                keib ^= ((long) data[tail + 8] & 0xff);
                keib *= ctsb;
                keib = Long.rotateLeft(keib, 33);
                keib *= ctsa;
                hashb ^= keib;
            case 8:
                keia ^= ((long) data[tail + 7] & 0xff) << 56;
            case 7:
                keia ^= ((long) data[tail + 6] & 0xff) << 48;
            case 6:
                keia ^= ((long) data[tail + 5] & 0xff) << 40;
            case 5:
                keia ^= ((long) data[tail + 4] & 0xff) << 32;
            case 4:
                keia ^= ((long) data[tail + 3] & 0xff) << 24;
            case 3:
                keia ^= ((long) data[tail + 2] & 0xff) << 16;
            case 2:
                keia ^= ((long) data[tail + 1] & 0xff) << 8;
            case 1:
                keia ^= ((long) data[tail] & 0xff);
                keia *= ctsa;
                keia = Long.rotateLeft(keia, 31);
                keia *= ctsb;
                hasha ^= keia;
                break;
            default:
                break;
        }
        hasha ^= length;
        hashb ^= length;
        hasha += hashb;
        hashb += hasha;
        hasha = fmix(hasha);
        hashb = fmix(hashb);
        hasha += hashb;
        hashb += hasha;
        final byte[] out = new byte[16];
        for (int idx = 0; idx < 8; ++idx) {
            out[idx] = (byte) (hasha >>> (idx * 8));
            out[idx + 8] = (byte) (hashb >>> (idx * 8));
        }
        return out;
    }

    private static long littleEndian(final byte[] data, final int index) {
        return ((long) data[index] & 0xff)
            | (((long) data[index + 1] & 0xff) << 8)
            | (((long) data[index + 2] & 0xff) << 16)
            | (((long) data[index + 3] & 0xff) << 24)
            | (((long) data[index + 4] & 0xff) << 32)
            | (((long) data[index + 5] & 0xff) << 40)
            | (((long) data[index + 6] & 0xff) << 48)
            | (((long) data[index + 7] & 0xff) << 56);
    }

    private static long fmix(final long value) {
        long result = value;
        result ^= result >>> 33;
        result *= 0xff51afd7ed558ccdL;
        result ^= result >>> 33;
        result *= 0xc4ceb9fe1a85ec53L;
        result ^= result >>> 33;
        return result;
    }
}
