/**
 * Copyright (C) 2011 Flamingo Project (http://www.cloudine.io).
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.engine.hawq.xml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.opencloudengine.flamingo2.engine.hawq.xml package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Queries_QNAME = new QName("http://www.flamingo2.com", "queries");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.opencloudengine.flamingo2.engine.hawq.xml
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link QueriesType }
     */
    public QueriesType createQueriesType() {
        return new QueriesType();
    }

    /**
     * Create an instance of {@link QueryType }
     */
    public QueryType createQueryType() {
        return new QueryType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueriesType }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.flamingo2.com", name = "queries")
    public JAXBElement<QueriesType> createQueries(QueriesType value) {
        return new JAXBElement<QueriesType>(_Queries_QNAME, QueriesType.class, null, value);
    }

}
