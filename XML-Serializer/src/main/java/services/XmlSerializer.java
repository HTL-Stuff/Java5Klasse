package services;


import lombok.SneakyThrows;
import services.domain.PrimitiveFields;

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
            throw new IllegalArgumentException("Object is null");
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
            System.out.println(field);

            if (field.getType().isPrimitive() || DEFAULT_WRAPPERS.contains(field.getType())) {
                System.out.println("it contains me");
                serializedField.append(serializePrimitive(field, object));
            } else if (field.getType().isArray()) {
                serializedField.append(serializeArray(field, object));
            } else if (Map.class.isAssignableFrom(field.getType())) {
                serializedField.append(serializeMap(field, object));
            } else if (Collection.class.isAssignableFrom(field.getType())) {
                serializedField.append(serializeCollection(field, object));
            } else {
                serializedField.append(serializeObject(field, object));
            }

            serialized.append(serializedField);
        }

        if (!DEFAULT_WRAPPERS.contains(clazz)) {
            serialized.append(convertToXmlTag(clazz.getSimpleName(), true));
        }

        System.out.println(serialized);
        return serialized.toString();
    }

    private String serializeCollection(Field field, Object object) {
        StringBuilder serialized = new StringBuilder();
        Collection<?> collection = (Collection<?>) getFieldValue(field, object);

        if (collection == null) return "";

        for (Object item : collection) {
            serialized.append(serialize(item));
        }

        return serialized.toString();
    }

    private String serializeMap(Field field, Object object) {
        StringBuilder serializedField = new StringBuilder(convertToXmlTag(field.getName()));

        Map<?, ?> map = (Map<?, ?>) getFieldValue(field, object);
        if (map == null) return "";

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            serializedField.append(convertKeyToXmlTag(entry.getKey()));
            serializedField.append(convertValueToXmlTag(entry.getValue()));
        }

        return serializedField.append(convertToXmlTag(field.getName(), true)).toString();
    }

    private String convertKeyToXmlTag(Object key) {
        return convertToXmlTag("key") +
                serialize(key) +
                convertToXmlTag("key", true);
    }

    private String convertValueToXmlTag(Object value) {
        return convertToXmlTag("value") +
                serialize(value) +
                convertToXmlTag("value", true);
    }

    private String serializeObject(Field field, Object object) {
        StringBuilder serializedField = new StringBuilder(convertToXmlTag(field.getName()));

        Object fieldValue = getFieldValue(field, object);
        if (fieldValue == null) return "";
        serializedField.append(serialize(fieldValue));

        return serializedField.append(convertToXmlTag(field.getName(), true)).toString();
    }

    private String serializeArray(Field field, Object object) {
        StringBuilder serializedArray = new StringBuilder(convertToXmlTag(field.getName()));
        Object[] array = (Object[]) getFieldValue(field, object);

        if (array == null) return "";

        for (Object arrayElement : array) {
            serializedArray.append(serialize(arrayElement));
        }

        return serializedArray.append(convertToXmlTag(field.getName(), true)).toString();
    }

    private String serializePrimitive(Field field, Object object) {
        StringBuilder serializedField = new StringBuilder(convertToXmlTag(field.getName()));

        System.out.println("primitive: " + field.getName());
        Object value = getFieldValue(field, object);

        System.out.println(value);
        if (value == null) return "";
        serializedField.append((value instanceof String) ? encode(value.toString()) : value);

        return serializedField.append(convertToXmlTag(field.getName(), true)).toString();
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
        XmlSerializer serializer = new XmlSerializer();

        var serialized = serializer.serialize(new PrimitiveFields('#', 42, true));
        System.out.println(serialized);
    }
}
