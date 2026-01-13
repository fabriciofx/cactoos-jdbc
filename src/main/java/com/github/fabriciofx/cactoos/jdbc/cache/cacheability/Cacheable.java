/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache.cacheability;

import com.github.fabriciofx.cactoos.jdbc.Query;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.config.CalciteConnectionConfigImpl;
import org.apache.calcite.config.CalciteConnectionProperty;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.jdbc.JavaTypeFactoryImpl;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.volcano.VolcanoPlanner;
import org.apache.calcite.prepare.CalciteCatalogReader;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rex.RexBuilder;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql.validate.SqlValidatorUtil;
import org.apache.calcite.sql2rel.SqlToRelConverter;
import org.apache.calcite.sql2rel.StandardConvertletTable;
import org.apache.calcite.tools.Frameworks;
import org.cactoos.Scalar;
import org.cactoos.scalar.Sticky;

/**
 * Cacheable.
 * @since 0.9.0
 */
public final class Cacheable implements Scalar<Boolean> {
    /**
     * Cacheability.
     */
    private final Scalar<Boolean> cacheability;

    /**
     * Ctor.
     * @param query A {@link Query}
     */
    public Cacheable(final Query query) {
        this.cacheability = new Sticky<>(
            () -> {
                final AtomicBoolean cacheable = new AtomicBoolean(true);
                final JavaTypeFactory types = new JavaTypeFactoryImpl();
                final SchemaPlus schema = Frameworks.createRootSchema(true);
                final SqlParser.Config config = SqlParser
                    .config()
                    .withCaseSensitive(false);
                final SqlParser parser = SqlParser.create(query.sql(), config);
                final SqlNode stmt = parser.parseQuery();
                final Set<String> tables = new HashSet<>();
                final Set<String> columns = new HashSet<>();
                stmt.accept(new Shuttle(cacheable, tables, columns));
                if (cacheable.get()) {
                    for (final String table : tables) {
                        schema.add(table, new Table(table, columns));
                    }
                    final Properties props = new Properties();
                    props.setProperty(
                        CalciteConnectionProperty.CASE_SENSITIVE.camelName(),
                        "false"
                    );
                    final CalciteCatalogReader reader = new CalciteCatalogReader(
                        CalciteSchema.from(schema),
                        Collections.singletonList(""),
                        types,
                        new CalciteConnectionConfigImpl(props)
                    );
                    final SqlValidator validator = SqlValidatorUtil.newValidator(
                        SqlStdOperatorTable.instance(),
                        reader,
                        types,
                        SqlValidator.Config.DEFAULT
                    );
                    final SqlNode validated = validator.validate(stmt);
                    final RelOptCluster cluster = RelOptCluster.create(
                        new VolcanoPlanner(),
                        new RexBuilder(types)
                    );
                    final SqlToRelConverter converter = new SqlToRelConverter(
                        null,
                        validator,
                        reader,
                        cluster,
                        StandardConvertletTable.INSTANCE,
                        SqlToRelConverter.config()
                    );
                    final RelNode relational = converter.convertQuery(
                        validated,
                        false,
                        true
                    ).rel;
                    new Visitor(cacheable).go(relational);
                }
                return cacheable.get();
            }
        );
    }

    @Override
    public Boolean value() throws Exception {
        return this.cacheability.value();
    }
}
