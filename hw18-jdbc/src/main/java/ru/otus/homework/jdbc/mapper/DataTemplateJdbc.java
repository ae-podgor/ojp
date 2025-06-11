package ru.otus.homework.jdbc.mapper;


import ru.otus.homework.core.repository.DataTemplate;
import ru.otus.homework.core.repository.DataTemplateException;
import ru.otus.homework.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        String selectByIdSql = entitySQLMetaData.getSelectByIdSql();
        return dbExecutor.executeSelect(connection, selectByIdSql, List.of(id), rs -> {
            try {
                if (rs.next()) {
                    T instance = entityClassMetaData.getConstructor().newInstance();
                    for (Field field : entityClassMetaData.getAllFields()) {
                        field.setAccessible(true);
                        String columnName = field.getName();
                        Object value = rs.getObject(columnName);
                        field.set(instance, value);
                    }
                    return instance;
                }
                return null;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        String selectAllSql = entitySQLMetaData.getSelectAllSql();
        return dbExecutor.executeSelect(connection, selectAllSql, Collections.emptyList(), rs -> {
                    try {
                        var list = new ArrayList<T>();
                        while (rs.next()) {
                            T instance = entityClassMetaData.getConstructor().newInstance();
                            for (Field field : entityClassMetaData.getAllFields()) {
                                field.setAccessible(true);
                                String columnName = field.getName();
                                Object value = rs.getObject(columnName);
                                field.set(instance, value);
                            }
                            list.add(instance);
                        }
                        return list;
                    } catch (Exception e) {
                        throw new DataTemplateException(e);
                    }
                })
                .orElseThrow(() -> new DataTemplateException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T object) {
        String insertSql = entitySQLMetaData.getInsertSql();
        try {
            List<Object> params = new ArrayList<>();
            for (Field field : entityClassMetaData.getFieldsWithoutId()) {
                field.setAccessible(true);
                Object value = field.get(object);
                params.add(value);
            }
            return dbExecutor.executeStatement(connection, insertSql, params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        String updateSql = entitySQLMetaData.getUpdateSql();
        try {
            List<Object> params = new ArrayList<>();
            for (Field field : entityClassMetaData.getFieldsWithoutId()) {
                field.setAccessible(true);
                Object value = field.get(object);
                params.add(value);
            }
            dbExecutor.executeStatement(connection, updateSql, List.of(params, entityClassMetaData.getIdField()));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
