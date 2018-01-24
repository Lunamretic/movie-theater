package ua.epam.spring.hometask.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Yuriy_Tkach
 */
public class Ticket extends DomainObject {

    private Long userId;

    private Long eventId;

    private LocalDateTime dateTime;

    private long seat;

    public Ticket() {
    }

    public Ticket(Long userId, Long eventId, LocalDateTime dateTime, long seat) {
        this.userId = userId;
        this.eventId = eventId;
        this.dateTime = dateTime;
        this.seat = seat;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public long getSeat() {
        return seat;
    }

    public void setSeat(long seat) {
        this.seat = seat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        if (seat != ticket.seat) return false;
        if (userId != null ? !userId.equals(ticket.userId) : ticket.userId != null) return false;
        if (!eventId.equals(ticket.eventId)) return false;
        return dateTime.equals(ticket.dateTime);
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + eventId.hashCode();
        result = 31 * result + dateTime.hashCode();
        result = 31 * result + (int) (seat ^ (seat >>> 32));
        return result;
    }
}
