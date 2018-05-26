package com.github.fabriciofx.cactoos.jdbc.stream;

import com.github.fabriciofx.cactoos.jdbc.DataStream;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.cactoos.Output;
import org.cactoos.Text;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public final class FormattedXmlDataStream implements DataStream {
    private final DataStream origin;
    private final int indent;

    public FormattedXmlDataStream(final DataStream strm) {
        this(strm, 2);
    }

    public FormattedXmlDataStream(final DataStream strm, final int indent) {
        this.origin = strm;
        this.indent = indent;
    }

    @Override
    public DataStream with(
        final String name,
        final Text value
    ) throws Exception {
        this.origin.with(name, value);
        return this;
    }

    @Override
    public void save(final Output output) throws Exception {
        this.origin.save(output);
    }

    @Override
    public String asString() throws Exception {
        return this.format(
            this.origin
                .asString()
                .replaceAll("(?m)^[ \t]*\r?\n", "")
                .replaceAll("[\r]*\n", ""),
            this.indent
        ).replaceAll("<\\?xml version.*\\?>[\r]*\n", "");
    }

    private String format(final String xml, final int indent) {
        try {
            final Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", indent + "");
            final StreamResult result = new StreamResult(new StringWriter());
            final DOMSource source = new DOMSource(this.parseXml(xml));
            transformer.transform(source, result);
            return result.getWriter().toString();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Document parseXml(final String xml) {
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory
                    .newInstance();
            final DocumentBuilder db = dbf.newDocumentBuilder();
            final InputSource is = new InputSource(new StringReader(xml));
            return db.parse(is);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
