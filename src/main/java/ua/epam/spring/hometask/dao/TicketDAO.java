package ua.epam.spring.hometask.dao;

import org.springframework.stereotype.Repository;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class TicketDAO extends AbstractDAO<Ticket> {

    public Collection<Ticket> addAll(Collection<Ticket> tickets) {
        tickets.forEach(ticket -> ticket.setId(idCounter.getAndIncrement()));
        tickets.forEach(ticket -> domainObjectMap.put(ticket.getId(), ticket));
        return tickets;
    }

    public Set<Ticket> getTicketsForEvent(@Nonnull Event event, @Nonnull LocalDateTime dateTime) {
        return domainObjectMap
                .values()
                .stream()
                .filter(ticket -> ticket.getEvent().equals(event) && ticket.getDateTime().equals(dateTime))
                .collect(Collectors.toSet());
    }

}
