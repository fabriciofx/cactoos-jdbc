/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.Adapter;
import java.io.StringWriter;
import java.sql.ResultSet;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * ContactAsXml.
 * Adapt a contact's ResultSet in XML.
 * @since 0.9.0
 */
public final class ContactAsXml implements Adapter<String> {
    @Override
    public String adapt(final ResultSet rset) throws Exception {
        final DocumentBuilderFactory docs = DocumentBuilderFactory
            .newInstance();
        docs.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        docs.setExpandEntityReferences(false);
        final Document doc = docs.newDocumentBuilder().newDocument();
        if (rset.next()) {
            final Element contact = doc.createElement("contact");
            doc.appendChild(contact);
            final Element phones = doc.createElement("phones");
            contact.appendChild(phones);
            final Element name = doc.createElement("name");
            name.setTextContent(rset.getString("name"));
            contact.appendChild(name);
            do {
                final Element phone = doc.createElement("phone");
                final Element number = doc.createElement("number");
                number.setTextContent(rset.getString("number"));
                phone.appendChild(number);
                final Element carrier = doc.createElement("carrier");
                carrier.setTextContent(rset.getString("carrier"));
                phone.appendChild(carrier);
                phones.appendChild(phone);
            } while (rset.next());
        }
        final Transformer transformer = TransformerFactory.newInstance()
            .newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        final StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.toString();
    }
}
