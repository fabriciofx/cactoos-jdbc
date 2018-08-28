/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018 Fabrício Barros Cabral
 *
 * Permission is hereby granted, free of charge, to def person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.fabriciofx.cactoos.jdbc;

import com.github.fabriciofx.cactoos.jdbc.type.AnyType;
import com.github.fabriciofx.cactoos.jdbc.type.BoolType;
import com.github.fabriciofx.cactoos.jdbc.type.DateTimeType;
import com.github.fabriciofx.cactoos.jdbc.type.DateType;
import com.github.fabriciofx.cactoos.jdbc.type.DecimalType;
import com.github.fabriciofx.cactoos.jdbc.type.IntType;
import com.github.fabriciofx.cactoos.jdbc.type.LongType;
import com.github.fabriciofx.cactoos.jdbc.type.RealType;
import com.github.fabriciofx.cactoos.jdbc.type.TextType;
import com.github.fabriciofx.cactoos.jdbc.type.UuidType;
import java.util.Iterator;
import java.util.List;
import org.cactoos.list.ListOf;

/**
 * Smart Data Types.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.2
 */
public final class SmartDataTypes implements DataTypes {
    /**
     * Types.
     */
    private final List<DataType<?>> types;

    private final DataType<?> def;

    /**
     * Ctor.
     */
    public SmartDataTypes() {
        this.types = new ListOf<>(
            new UuidType(),
            new TextType(),
            new IntType(),
            new DateTimeType(),
            new DateType(),
            new DecimalType(),
            new BoolType(),
            new LongType(),
            new RealType()
        );
        this.def = new AnyType();
    }

    @Override
    public DataType<?> type(final int code) {
        DataType<?> result = this.def;
        for (final DataType<?> type : this.types) {
            if (type.match(code)) {
                result = type;
                break;
            }
        }
        return result;
    }

    @Override
    public Iterator<DataType<?>> iterator() {
        return this.types.iterator();
    }
}
