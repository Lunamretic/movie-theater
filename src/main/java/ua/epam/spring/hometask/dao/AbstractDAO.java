package ua.epam.spring.hometask.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ua.epam.spring.hometask.domain.DomainObject;
import ua.epam.spring.hometask.domain.Event;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractDAO <T extends DomainObject> {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public abstract T add(T domainObject);

    public void remove(T domainObject) {
        long id = domainObject.getId();
        jdbcTemplate.update(getSqlDelete(), id);
    }

    public T getById(Long id) {
        T t;
        try {
            t = jdbcTemplate.queryForObject(
                    getSqlSelectById(),
                    new Object[]{id},
                    getRowMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return t;
    }

    public abstract Collection<T> getAll();

    protected abstract String getSqlInsert();

    protected abstract String getSqlDelete();

    protected abstract String getSqlSelectById();

    protected abstract String getSqlSelectAll();

    protected abstract RowMapper<T> getRowMapper();

}