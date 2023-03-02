package services;


import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class XmlSerializer {

    private static final List<Class<?>> DEFAULT_WRAPPERS = List.of(
            String.class,
            Boolean.class,
            Character.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            Void.class
    );

    @SneakyThrows
    public String serialize(Object object) {
        if (object == null) {
            return "";
        }

        StringBuilder serialized = new StringBuilder();
        Class<?> clazz = object.getClass();

        if (!DEFAULT_WRAPPERS.contains(clazz)) {
            serialized.append(convertToXmlTag(clazz.getSimpleName()));
        }

        for (Field field : object.getClass().getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()))
                continue;
            StringBuilder serializedField = new StringBuilder();

            Object value = getFieldValue(field, object);

            if (value == null) {
                continue;
            }

            switch (value) {
                case Object[] array when field.getType().isArray() ->
                        serializedField.append(serializeArray(field.getName(), array));
                case Map<?, ?> map -> serializedField.append(serializeMap(field.getName(), map));
                case Collection<?> collection ->
                        serializedField.append(serializeArray(field.getName(), collection.toArray()));
                case Object obj when DEFAULT_WRAPPERS.contains(value.getClass()) || value.getClass().isPrimitive() ->
                        serializedField.append(serializePrimitive(field.getName(), obj));
                case Object obj -> serializedField.append(serializeObject(field.getName(), obj));
            }

            serialized.append(serializedField);
        }

        if (!DEFAULT_WRAPPERS.contains(clazz)) {
            serialized.append(convertToXmlTag(clazz.getSimpleName(), true));
        }

        System.out.println(serialized);
        return serialized.toString();
    }


    private String serializeCollection(Collection<?> collection) {
        StringBuilder serialized = new StringBuilder();

        for (Object item : collection) {
            serialized.append(serialize(item));
        }

        return serialized.toString();
    }

    private String serializeMap(String name, Map<?, ?> map) {
        StringBuilder serializedField = new StringBuilder(convertToXmlTag(name));

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            serializedField.append(convertKeyToXmlTag(entry.getKey()));
            serializedField.append(convertValueToXmlTag(entry.getValue()));
        }

        return serializedField.append(convertToXmlTag(name, true)).toString();
    }

    private String convertKeyToXmlTag(Object key) {
        return convertToXmlTag("key") +
                key +
                convertToXmlTag("key", true);
    }

    private String convertValueToXmlTag(Object value) {
        return convertToXmlTag("value") +
                serialize(value) +
                convertToXmlTag("value", true);
    }

    private String serializeObject(String name, Object fieldValue) {
        return convertToXmlTag(name) + serialize(fieldValue) + convertToXmlTag(name, true);
    }

    private String serializeArray(String name, Object[] array) {
        StringBuilder serializedArray = new StringBuilder(convertToXmlTag(name));


        for (Object arrayElement : array) {
            serializedArray.append(convertToXmlTag("value"));
            serializedArray.append(arrayElement);
            serializedArray.append(convertToXmlTag("value", true));
        }

        return serializedArray.append(convertToXmlTag(name, true)).toString();
    }

    private String serializePrimitive(String name, Object value) {
        StringBuilder serializedField = new StringBuilder(convertToXmlTag(name));

        if (value == null) return "";
        serializedField.append((value instanceof String) ? encode(value.toString()) : value);

        return serializedField.append(convertToXmlTag(name, true)).toString();
    }

    private String convertToXmlTag(String toConvert) {
        return convertToXmlTag(toConvert, false);
    }

    private String convertToXmlTag(String toConvert, boolean isClosingTag) {
        if (toConvert == null) {
            throw new IllegalArgumentException("String to convert is null");
        }

        if (toConvert.isBlank()) {
            throw new IllegalArgumentException("String to convert is blank");
        }

        if (isClosingTag) {
            return "</" + toConvert + ">";
        }

        return "<" + toConvert + ">";
    }

    private String encode(String toConvert) {
        return toConvert.chars().mapToObj(c -> (char) c)
                .map(c -> switch (c) {
                    case '<' -> "&lt;";
                    case '>' -> "&gt;";
                    case '&' -> "&amp;";
                    case '"' -> "&quot;";
                    case '\'' -> "&apos;";
                    default -> c.toString();
                })
                .collect(Collectors.joining());
    }

    private Object getFieldValue(Field field, Object object) {
        Method getter = getGetter(field, object);

        if (getter == null) {
            return null;
        }

        return invokeMethod(getter, object);
    }

    private Object invokeMethod(Method getter, Object object) {
        try {
            return getter.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Method getGetter(Field field, Object object) {
        String getter = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        Method method = getMethod("is" + getter, object);

        if (method == null) {
            method = getMethod("get" + getter, object);
            if (method == null) {
                method = getMethod(field.getName(), object);
            }
        }

        return method;
    }

    private Method getMethod(String getter, Object object) {
        System.out.println(object.getClass().getSimpleName());
        System.out.println(Arrays.toString(Arrays.stream(object.getClass().getMethods()).filter(m -> m.getName().contains(getter)).toArray()));
        try {
            return object.getClass().getMethod(getter);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        Integer wrappers = 1;
        System.out.println(new XmlSerializer().serialize(wrappers));
    }
}
