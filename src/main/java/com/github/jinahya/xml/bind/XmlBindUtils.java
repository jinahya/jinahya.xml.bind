package com.github.jinahya.xml.bind;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static java.util.Objects.requireNonNull;

public final class XmlBindUtils {

    public static void marshal(final Marshaller marshaller, final Object jaxbElement, final Object target)
            throws JAXBException {
        requireNonNull(marshaller, "marshaller is null");
        requireNonNull(jaxbElement, "jaxbElement is null");
        requireNonNull(target, "target is null");
        final Method method = Arrays.stream(Marshaller.class.getMethods())
                .filter(m -> "marshal".equals(m.getName()))
                .filter(m -> {
                    final Class<?>[] parameterTypes = m.getParameterTypes();
                    return parameterTypes.length == 2 && parameterTypes[1].isAssignableFrom(target.getClass());
                })
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("no 'marshal' method found for " + target));
        try {
            method.invoke(marshaller, jaxbElement, target);
        } catch (final ReflectiveOperationException roe) {
            final Throwable cause = roe.getCause();
            if (roe instanceof InvocationTargetException && cause instanceof JAXBException) {
                throw (JAXBException) cause;
            }
            throw new RuntimeException("failed to marshal to " + target, cause);
        }
    }


    static QName qualifiedName(String localPart) {
        requireNonNull(localPart, "localPart is null");
        if ((localPart = localPart.trim()).isEmpty()) {
            throw new IllegalArgumentException("localPart is blank");
        }
        return new QName(XmlConstants.NS_URI_DATABASE_METADATA_BIND, localPart);
    }

    private XmlBindUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
