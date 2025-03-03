/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018-2025 Fabrício Barros Cabral
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.fabriciofx.cactoos.jdbc.adapter;

import com.github.fabriciofx.cactoos.jdbc.Adapter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Result as XML.
 *
 * @since 0.4
 */
@SuppressWarnings(
    {
        "PMD.AvoidCatchingGenericException",
        "PMD.AvoidThrowingRawExceptionTypes"
    }
)
public final class ResultSetAsXml implements Adapter<String> {
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
     * @param root A root tag
     * @param child A child tag
     */
    public ResultSetAsXml(final String root, final String child) {
        this.root = root;
        this.child = child;
    }

    @Override
    public String adapt(final ResultSet rset) {
        try {
            final DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();
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
            // @checkstyle IllegalCatchCheck (1 line)
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
