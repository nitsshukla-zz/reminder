package com.amazon.reminder;

import java.lang.reflect.Field;

public class TestUtil {
    public static void putPrivate(String fieldName, Object objectToBeInserted,
                                  Object objectHavingField) throws Exception {
        Class classObj = objectHavingField.getClass();
        putPrivate(fieldName, objectToBeInserted, objectHavingField, classObj);
    }

    public static <T> T getPrivate(Object object,
                                   String fieldName, Class T) throws Exception {
        Class classHandler = object.getClass();
        Field field = classHandler.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(object);
    }

    public static String getPrivateStatic(Class classHandler,
                                          String fieldName) throws NoSuchFieldException, IllegalAccessException {
        return getPrivateStatic(classHandler, fieldName, String.class);
    }

    public static <T> T getPrivateStatic(Class classHandler,
                                         String fieldName,
                                         Class T) throws NoSuchFieldException, IllegalAccessException {
        Field field = classHandler.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(null);
    }

    public static void putPrivate(String fieldName, Object objectToBeInserted,
                                  Object objectHavingField,
                                  Class classObj) throws NoSuchFieldException, IllegalAccessException {
        Field field = classObj.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(objectHavingField, objectToBeInserted);
    }
}
