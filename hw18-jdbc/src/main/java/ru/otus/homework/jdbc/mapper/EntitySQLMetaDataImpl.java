package ru.otus.homework.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaDataClient) {
        this.entityClassMetaData = entityClassMetaDataClient;
    }

    @Override
    public String getSelectAllSql() {
        return "select * from %s".formatted(entityClassMetaData.getName().toLowerCase());
    }

    @Override
    public String getSelectByIdSql() {
        return "select * from %s where %s = ?"
                .formatted(entityClassMetaData.getName().toLowerCase(), entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        String fieldNames = fieldsWithoutId.stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));

        // Создаем плейсхолдеры для всех значений, заменяя одиночный '?' на корректное количество '?'
        String placeholders = fieldsWithoutId.stream()
                .map(field -> "?")
                .collect(Collectors.joining(", "));

        return "insert into %s (%s) values (%s)"
                .formatted(entityClassMetaData.getName().toLowerCase(), fieldNames, placeholders);
    }

    @Override
    public String getUpdateSql() {
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        String clause = fieldsWithoutId.stream()
                .map(field -> field.getName() + " = ?")
                .collect(Collectors.joining(", "));
        return "update %s set %s where %s = ?"
                .formatted(entityClassMetaData.getName().toLowerCase(),
                        clause,
                        entityClassMetaData.getIdField().getName());
    }
}
