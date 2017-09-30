package ua.epam.spring.hometask.dao;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TicketDAO extends AbstractDAO<Ticket> {

    private static Map<Long, Ticket> ticketMap;

    static {
        ticketMap = new HashMap<>();
    }

    public Collection<Ticket> addAll(Collection<Ticket> tickets) {
        tickets.forEach(ticket -> ticketMap.put(ticket.getId(), ticket));
        return tickets;
    }

    public Set<Ticket> getTicketsForEvent(@Nonnull Event event, @Nonnull LocalDateTime dateTime) {
        return ticketMap
                .values()
                .stream()
                .filter(ticket -> ticket.getEvent().equals(event) && ticket.getDateTime().equals(dateTime))
                .collect(Collectors.toSet());
    }

    @Override
    public Ticket add(Ticket ticket) {
        return ticketMap.put(ticket.getId(), ticket);
    }

    @Override
    public void remove(Ticket ticket) {
        ticketMap.remove(ticket.getId());
    }

    @Override
    public Ticket getById(Long id) {
        return ticketMap.get(id);
    }

    @Override
    public Collection<Ticket> getAll() {
        return ticketMap.values();
    }
}
