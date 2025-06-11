package ru.otus.homework.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private final String selectAllQuery;
    private final String selectByIdQuery;
    private final String insertQuery;
    private final String updateQuery;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.selectAllQuery = initSelectAll(entityClassMetaData);
        this.selectByIdQuery = initSelectById(entityClassMetaData);
        this.insertQuery = initInsert(entityClassMetaData);
        this.updateQuery = initUpdate(entityClassMetaData);
    }

    @Override
    public String getSelectAllSql() {
        return selectAllQuery;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdQuery;
    }

    @Override
    public String getInsertSql() {
        return insertQuery;
    }

    @Override
    public String getUpdateSql() {
        return updateQuery;
    }

    private String initUpdate(EntityClassMetaData<T> entityClassMetaData) {
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        String clause = fieldsWithoutId.stream()
                .map(field -> field.getName() + " = ?")
                .collect(Collectors.joining(", "));
        return "update %s set %s where %s = ?"
                .formatted(entityClassMetaData.getName().toLowerCase(),
                        clause,
                        entityClassMetaData.getIdField().getName());

    }

    private String initInsert(EntityClassMetaData<T> entityClassMetaData) {
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        String fieldNames = fieldsWithoutId.stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));

        String placeholders = fieldsWithoutId.stream()
                .map(field -> "?")
                .collect(Collectors.joining(", "));

        return "insert into %s (%s) values (%s)"
                .formatted(entityClassMetaData.getName().toLowerCase(), fieldNames, placeholders);
    }

    private String initSelectById(EntityClassMetaData<T> entityClassMetaData) {
        return "select * from %s where %s = ?"
                .formatted(entityClassMetaData.getName().toLowerCase(), entityClassMetaData.getIdField().getName());
    }

    private String initSelectAll(EntityClassMetaData<T> entityClassMetaData) {
        return "select * from %s".formatted(entityClassMetaData.getName().toLowerCase());
    }
}
