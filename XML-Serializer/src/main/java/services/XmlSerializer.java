package services;


import lombok.SneakyThrows;
import services.domain.PrimitiveFields;

import java.lang.reflect.Field;

public class XmlSerializer {


    @SneakyThrows
    public String serialize(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Object is null");
        }

        StringBuilder serialized = new StringBuilder();
        String className = object.getClass().getSimpleName();

        serialized.append(convertToXmlTag(className));

        for (Field field : object.getClass().getDeclaredFields()) {
            StringBuilder serializedField = new StringBuilder();
            System.out.println(field);
            field.setAccessible(true);

            serializedField.append(convertToXmlTag(field.getName()));

            if (field.getType().isPrimitive() || (field.getType() == String.class)) {
                Object fieldObject = field.get(object);
                if (fieldObject == null) {
                    continue;
                }
                serializedField.append(field.get(object));
            } else if (field.getType().isArray()) {
                Object[] array = (Object[]) field.get(object);
                if (array == null) {
                    continue;
                }
                for (Object arrayObject : array) {
                    System.out.println(arrayObject);
                    serializedField.append(serialize(arrayObject));
                }
            } else {
                Object fieldObject = field.get(object);
                if (fieldObject == null) {
                    continue;
                }
                serializedField.append(serialize(fieldObject));
            }

            serialized.append(serializedField.append(convertToXmlTag(field.getName(), true)));
        }

        serialized.append(convertToXmlTag(className, true));


        return serialized.toString();
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

    public static void main(String[] args) {
        XmlSerializer serializer = new XmlSerializer();

        var serialized = serializer.serialize(new PrimitiveFields('#', 42, true));
        System.out.println(serialized);
    }
}
