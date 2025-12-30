/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.scalar;

import com.github.fabriciofx.cactoos.jdbc.Statement;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Locale;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.cactoos.Scalar;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * ResultSet as XML.
 *
 * @since 0.4
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class ResultSetAsXml implements Scalar<String> {
    /**
     * Statement that returns a ResultSet.
     */
    private final Statement<ResultSet> statement;

    /**
     * Root tag in the XML.
     */
    private final String root;

    /**
     * Child tag in the XML.
     */
    private final String child;

    /**
     * Ctor.
     * @param stmt A statement
     * @param root A root tag
     * @param child A child tag
     */
    public ResultSetAsXml(
        final Statement<ResultSet> stmt,
        final String root,
        final String child
    ) {
        this.statement = stmt;
        this.root = root;
        this.child = child;
    }

    @Override
    public String value() throws Exception {
        try (ResultSet rset = this.statement.execute()) {
            final DocumentBuilderFactory factory = DocumentBuilderFactory
                .newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setExpandEntityReferences(false);
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document doc = builder.newDocument();
            final Element entries = doc.createElement(this.root);
            doc.appendChild(entries);
            final ResultSetMetaData rsmd = rset.getMetaData();
            final int cols = rsmd.getColumnCount();
            while (rset.next()) {
                final Element entry = doc.createElement(this.child);
                entries.appendChild(entry);
                for (int idx = 1; idx <= cols; ++idx) {
                    final String name = rsmd.getColumnName(idx)
                        .toLowerCase(Locale.ENGLISH);
                    final Object value = rset.getObject(idx);
                    final Element node = doc.createElement(name);
                    node.appendChild(doc.createTextNode(value.toString()));
                    entry.appendChild(node);
                }
            }
            final DOMSource src = new DOMSource(doc);
            final Transformer transformer = TransformerFactory.newInstance()
                .newTransformer();
            transformer.setOutputProperty(
                OutputKeys.OMIT_XML_DECLARATION,
                "yes"
            );
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            final StringWriter writer = new StringWriter();
            final StreamResult result = new StreamResult(writer);
            transformer.transform(src, result);
            return writer.toString();
        }
    }
}
