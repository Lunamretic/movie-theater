package ua.epam.spring.hometask.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Repository
public class UserDAO extends AbstractDAO<User> {

    private static final String SQL_INSERT = "INSERT INTO users (firstName, lastName, email) VALUES (?, ?, ?)";

    private static final String SQL_DELETE = "DELETE FROM users WHERE id = ?";

    private static final String SQL_SELECT_BY_ID = "SELECT * FROM users WHERE id = ?";

    private static final String SQL_SELECT_ALL = "SELECT * FROM users";

    private static final String SQL_SELECT_BY_EMAIL = "SELECT * FROM users WHERE email = ?";

    @Override
    public User add(User domainObject) {
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, domainObject.getFirstName());
            ps.setString(2, domainObject.getLastName());
            ps.setString(3, domainObject.getEmail());
            return ps;
        };
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(preparedStatementCreator, keyHolder);

        Long id = keyHolder.getKey().longValue();
        domainObject.setId(id);

        return domainObject;
    }

    @Override
    public Collection<User> getAll() {
        Collection<User> users = new ArrayList<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL_SELECT_ALL);
        for (Map row : rows) {
            User user = new User();
            user.setId((Long)row.get("id"));
            user.setFirstName((String)row.get("firstName"));
            user.setLastName((String)row.get("lastName"));
            user.setEmail((String)row.get("email"));
            users.add(user);
        }
        return users;
    }

    @Nullable
    public User findByEmail(@Nonnull String email) {
        User user;
        try {
            user = jdbcTemplate.queryForObject(
                    SQL_SELECT_BY_EMAIL,
                    new Object[]{email},
                    getRowMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return user;
    }

    public boolean updateTickets(@Nonnull Set<Ticket> tickets, @Nonnull Long id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    protected String getSqlInsert() {
        return SQL_INSERT;
    }

    protected String getSqlDelete() {
        return SQL_DELETE;
    }

    protected String getSqlSelectById() {
        return SQL_SELECT_BY_ID;
    }

    protected String getSqlSelectAll() {
        return SQL_SELECT_ALL;
    }

    protected RowMapper<User> getRowMapper() {
        return (resultSet, i) -> {
            Long id = resultSet.getLong("id");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String email = resultSet.getString("email");

            User user = new User();
            user.setId(id);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);

            return user;
        };
    }

}
