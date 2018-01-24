package ua.epam.spring.hometask.dao;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.epam.spring.hometask.domain.Ticket;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class TicketDAO extends AbstractDAO<Ticket> {

    private static final String SQL_INSERT = "INSERT INTO tickets (user_id, event_id, dateTime, seat) VALUES (?, ?, ?, ?)";

    private static final String SQL_DELETE = "DELETE FROM tickets WHERE id = ?";

    private static final String SQL_SELECT_BY_ID = "SELECT * FROM tickets WHERE id = ?";

    private static final String SQL_SELECT_ALL = "SELECT * FROM tickets";

    private static final String SQL_SELECT_BY_EVENT = "SELECT * FROM tickets WHERE event_id = ? AND dateTime = ?";

    @Override
    public Ticket add(Ticket domainObject) {
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, domainObject.getUserId());
            ps.setLong(2, domainObject.getEventId());
            ps.setObject(3, domainObject.getDateTime());
            ps.setLong(4, domainObject.getSeat());
            return ps;
        };
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(preparedStatementCreator, keyHolder);

        Long id = keyHolder.getKey().longValue();
        domainObject.setId(id);

        return domainObject;
    }

    @Override
    public Collection<Ticket> getAll() {
        Collection<Ticket> tickets = new ArrayList<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL_SELECT_ALL);
        for (Map row : rows) {
            Ticket ticket = new Ticket();
            ticket.setId((Long)row.get("id"));
            ticket.setUserId((Long)row.get("userId"));
            ticket.setEventId((Long)row.get("eventId"));
            ticket.setDateTime((LocalDateTime)row.get("dateTime"));
            ticket.setSeat((Long)row.get("seat"));
            tickets.add(ticket);
        }
        return tickets;
    }

    public Collection<Ticket> addAll(Collection<Ticket> tickets) {
        Collection<Ticket> ticketsWithId = new ArrayList<>();

        tickets.forEach(ticket -> ticketsWithId.add(add(ticket)));

        return ticketsWithId;
    }

    public Set<Ticket> getTicketsForEvent(@Nonnull Long eventId, @Nonnull LocalDateTime dateTime) {
        Set<Ticket> tickets = new HashSet<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL_SELECT_BY_EVENT, eventId, dateTime);
        for (Map row : rows) {
            Ticket ticket = new Ticket();
            ticket.setId((Long)row.get("id"));
            ticket.setUserId((Long)row.get("userId"));
            ticket.setEventId((Long)row.get("eventId"));
            ticket.setDateTime((LocalDateTime)row.get("dateTime"));
            ticket.setSeat((Long)row.get("seat"));
            tickets.add(ticket);
        }
        return tickets;
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

    protected RowMapper<Ticket> getRowMapper() {
        return (resultSet, i) -> {
            Long id = resultSet.getLong("id");
            Long userId = resultSet.getLong("user_id");
            Long eventId = resultSet.getLong("event_id");
            LocalDateTime dateTime = (LocalDateTime) resultSet.getObject("dateTime");
            Long seat = resultSet.getLong("seat");

            Ticket ticket = new Ticket();
            ticket.setId(id);
            ticket.setUserId(userId);
            ticket.setEventId(eventId);
            ticket.setDateTime(dateTime);
            ticket.setSeat(seat);

            return ticket;
        };
    }

}
