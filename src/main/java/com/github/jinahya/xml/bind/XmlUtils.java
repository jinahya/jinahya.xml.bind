package com.github.jinahya.xml.bind;

import javax.xml.namespace.QName;

import static java.util.Objects.requireNonNull;

public final class XmlUtils {

    static QName qualifiedName(String localPart) {
        requireNonNull(localPart, "localPart is null");
        if ((localPart = localPart.trim()).isEmpty()) {
            throw new IllegalArgumentException("localPart is blank");
        }
        return new QName(XmlConstants.NS_URI_DATABASE_METADATA_BIND, localPart);
    }

    private XmlUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
