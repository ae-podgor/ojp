package ru.otus.homework.jdbc.mapper;

import ru.otus.homework.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final String name;
    private final Constructor<T> constructor;
    private final List<Field> allFields;
    private final List<Field> noIdFields;
    private final Field idField;

    public EntityClassMetaDataImpl(Class<T> entityClass) {
        this.name = entityClass.getSimpleName();
        this.constructor = initConstructor(entityClass);
        this.allFields = List.of(entityClass.getDeclaredFields());
        this.noIdFields = initNoIdFields(entityClass);
        this.idField = initIdField(entityClass);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return noIdFields;
    }

    private List<Field> initNoIdFields(Class<T> entityClass) {
        List<Field> fields = new ArrayList<>();
        for (Field field : entityClass.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Id.class)) {
                fields.add(field);
            }
        }
        return fields;
    }

    private Constructor<T> initConstructor(Class<T> entityClass) {
        try {
            return entityClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Default constructor not found", e);
        }
    }

    private Field initIdField(Class<T> entityClass) {
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        throw new RuntimeException("Field with @Id annotation not found");
    }
}
