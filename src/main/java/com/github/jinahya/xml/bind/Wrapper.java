package com.github.jinahya.xml.bind;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.transform.Source;
import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

// http://blog.bdoughan.com/2012/11/creating-generic-list-wrapper-in-jaxb.html
// http://blog.bdoughan.com/2010/12/jaxb-and-immutable-objects.html
public final class Wrapper<T> {

    /**
     * Unmarshalls a list of specified type from specified source.
     * <blockquote><pre>{@code
     * List<Category> categories = unmarshal(Category.class, new StreamSource(new File("categories.xml"));
     * }</pre></blockquote>
     *
     * @param type   the element type.
     * @param source the source from which elements are unmarshalled.
     * @param <T>    element type parameter
     * @return a list of unmarshalled instances of {@code type}.
     * @throws JAXBException if failed to unmarshal.
     */
    @SuppressWarnings({"unchecked"})
    public static <T> List<T> unmarshal(final Class<T> type, final Source source) throws JAXBException {
        requireNonNull(type, "type is null");
        requireNonNull(source, "source is null");
        final JAXBContext context = JAXBContext.newInstance(Wrapper.class, type);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final Wrapper<T> wrapper = unmarshaller.unmarshal(source, Wrapper.class).getValue();
        return wrapper.elements;
    }

    /**
     * Marshals given elements of specified type to specified target.
     * <blockquote><pre>{@code
     * List<Category> categories = getCategories();
     * marshal(Category.class, categories, "categories", new File("categories.xml");
     * }</pre></blockquote>
     *
     * @param type     the element type.
     * @param elements the elements to marshal.
     * @param target   the target to which elements are marshalled.
     * @param operator an operator for decorating the marshaller; may be {@code null};
     * @param <T>      element type parameter
     * @throws JAXBException if failed to marshal.
     * @see #marshal(Class, List, Object)
     */
    @SuppressWarnings({"unchecked"})
    public static <T> void marshal(final Class<T> type, final List<T> elements, final Object target,
                                   final UnaryOperator<Marshaller> operator)
            throws JAXBException {
        requireNonNull(type, "type is null");
        requireNonNull(elements, "elements is null");
        requireNonNull(target, "target is null");
        final JAXBContext context = JAXBContext.newInstance(Wrapper.class, type);
        final JAXBElement<Wrapper<T>> wrapped = new JAXBElement<>(
                XmlBindUtils.qualifiedName(Introspector.decapitalize(Wrapper.class.getSimpleName())),
                (Class<Wrapper<T>>) (Class<?>) Wrapper.class, Wrapper.of(elements));
        Marshaller marshaller = context.createMarshaller();
        if (operator != null) {
            marshaller = operator.apply(marshaller);
        }
        XmlBindUtils.marshal(marshaller, wrapped, target);
    }

    /**
     * Marshals given elements of specified type to specified target.
     * <blockquote><pre>{@code
     * List<Category> categories = getCategories();
     * marshal(Category.class, categories, "categories", new File("categories.xml");
     * }</pre></blockquote>
     *
     * @param type     the element type.
     * @param elements the elements to marshal.
     * @param target   the target to which elements are marshalled.
     * @param <T>      element type parameter
     * @throws JAXBException if failed to marshal.
     * @see #marshal(Class, List, Object, UnaryOperator)
     */
    public static <T> void marshal(final Class<T> type, final List<T> elements, final Object target)
            throws JAXBException {
        marshal(type, elements, target, null);
    }

    private static <T> Wrapper<T> of(final List<T> elements) {
        requireNonNull(elements, "elements is null");
        final Wrapper<T> instance = new Wrapper<>();
        instance.elements = elements;
        return instance;
    }

    private Wrapper() {
        super();
    }

    @XmlAnyElement(lax = true)
    private List<T> elements;
}
