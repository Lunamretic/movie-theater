package ua.epam.spring.hometask.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.EventRating;
import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Repository
public class EventDAO extends AbstractDAO<Event> {

    private static final String SQL_INSERT = "INSERT INTO events (name, basePrice, rating) VALUES (?, ?, ?)";

    private static final String SQL_DELETE = "DELETE FROM events WHERE id = ?";

    private static final String SQL_SELECT_BY_ID = "SELECT * FROM events WHERE id = ?";

    private static final String SQL_SELECT_ALL = "SELECT * FROM events";

    private static final String SQL_SELECT_BY_NAME = "SELECT * FROM events WHERE name = ?";

    @Override
    public Event add(Event domainObject) {
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, domainObject.getName());
            ps.setDouble(2, domainObject.getBasePrice());
            ps.setString(3, domainObject.getRating().toString());
            return ps;
        };
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(preparedStatementCreator, keyHolder);

        Long id = keyHolder.getKey().longValue();
        domainObject.setId(id);

        return domainObject;
    }

    @Override
    public Collection<Event> getAll() {
        Collection<Event> events = new ArrayList<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL_SELECT_ALL);
        for (Map row : rows) {
            Event event = new Event();
            event.setId((Long)row.get("id"));
            event.setName((String)row.get("name"));
            event.setBasePrice((Double)row.get("basePrice"));
            event.setRating(EventRating.valueOf((String)row.get("rating")));
        }
        return events;
    }

    @Nullable
    public Event findByName(@Nonnull String name) {
        Event event;
        try {
            event = jdbcTemplate.queryForObject(
                    SQL_SELECT_BY_NAME,
                    new Object[]{name},
                    getRowMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return event;
    }

    @Override
    protected String getSqlInsert() {
        return SQL_INSERT;
    }

    @Override
    protected String getSqlDelete() {
        return SQL_DELETE;
    }

    @Override
    protected String getSqlSelectById() {
        return SQL_SELECT_BY_ID;
    }

    @Override
    protected String getSqlSelectAll() {
        return SQL_SELECT_ALL;
    }

    @Override
    protected RowMapper<Event> getRowMapper() {
        return (resultSet, i) -> {
            Long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            double basePrice = resultSet.getDouble("basePrice");
            EventRating eventRating = EventRating.valueOf(resultSet.getString("rating"));

            Event event = new Event();
            event.setId(id);
            event.setName(name);
            event.setBasePrice(basePrice);
            event.setRating(eventRating);

            return event;
        };
    }
}