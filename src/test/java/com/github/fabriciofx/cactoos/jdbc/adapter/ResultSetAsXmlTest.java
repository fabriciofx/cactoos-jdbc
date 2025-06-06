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

import com.github.fabriciofx.cactoos.jdbc.Server;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.script.ScriptOf;
import com.github.fabriciofx.cactoos.jdbc.server.H2Server;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.jcabi.matchers.XhtmlMatchers;
import java.sql.ResultSet;
import org.cactoos.io.ResourceOf;
import org.cactoos.text.Joined;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

/**
 * StatementSelect tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings(
    {
        "PMD.AvoidInstantiatingObjectsInLoops",
        "PMD.AvoidDuplicateLiterals",
        "PMD.TestClassWithoutTestCases"
    }
)
final class ResultSetAsXmlTest {
    @Test
    void xml() throws Exception {
        try (
            Server server = new H2Server(
                new ScriptOf(
                    new ResourceOf(
                        new Joined(
                            "/",
                            "com/github/fabriciofx/cactoos/jdbc/phonebook",
                            "phonebook-h2.sql"
                        )
                    )
                )
            )
        ) {
            server.start();
            try (
                ResultSet rset = new Select(
                    server.session(),
                    new QueryOf(
                        new Joined(
                            " ",
                            "SELECT name, number, carrier FROM contact",
                            "JOIN phone ON contact.id = phone.contact_id"
                        )
                    )
                ).result()
            ) {
                MatcherAssert.assertThat(
                    "Must convert a contact into a XML",
                    XhtmlMatchers.xhtml(
                        new ResultSetAsXml(
                            "contacts",
                            "contact"
                        ).adapt(rset)
                    ),
                    XhtmlMatchers.hasXPaths(
                        "/contacts/contact/name[text()='Joseph Klimber']",
                        "/contacts/contact/number[text()='(87)99991-1234']",
                        "/contacts/contact/carrier[text()='TIM']",
                        "/contacts/contact/number[text()='(87)89234-9876']",
                        "/contacts/contact/carrier[text()='Oi']"
                    )
                );
            }
        }
    }
}
