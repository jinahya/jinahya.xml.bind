@XmlSchema(
        attributeFormDefault = XmlNsForm.UNQUALIFIED,
        elementFormDefault = XmlNsForm.QUALIFIED,
        namespace = XmlConstants.NS_URI_DATABASE_METADATA_BIND,
        xmlns = {
                @XmlNs(prefix = XMLConstants.DEFAULT_NS_PREFIX,
                       namespaceURI = XmlConstants.NS_URI_DATABASE_METADATA_BIND),
                @XmlNs(prefix = "xsi", namespaceURI = XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI)
        }
)
@XmlAccessorType(XmlAccessType.NONE)
package com.github.jinahya.xml.bind;

import javax.xml.XMLConstants;
import javax.xml.bind.annotation.*;
