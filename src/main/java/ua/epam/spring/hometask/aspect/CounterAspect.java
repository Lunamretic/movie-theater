package ua.epam.spring.hometask.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Aspect
@Component
public class CounterAspect {

    private Map<Long, Long> counterEventGetByName;

    private Map<Long, Long> counterEventGetPrice;

    private Map<Long, Long> counterEventBooked;

    {
        counterEventGetByName = new LinkedHashMap<>();
        counterEventGetPrice = new LinkedHashMap<>();
        counterEventBooked = new LinkedHashMap<>();
    }

    public Map<Long, Long> getCounterEventGetByName() {
        return counterEventGetByName;
    }

    public Map<Long, Long> getCounterEventGetPrice() {
        return counterEventGetPrice;
    }

    public Map<Long, Long> getCounterEventBooked() {
        return counterEventBooked;
    }

    @Pointcut("target(ua.epam.spring.hometask.service.BookingService)")
    private void withinBookingService() {}

    @AfterReturning(
            pointcut = "execution(* getByName(..)) && target(ua.epam.spring.hometask.service.EventService)",
            returning = "eventId"
    )
    private void afterEventServiceGetByName(Long eventId) {
        if (eventId != null) {
            counterEventGetByName.computeIfPresent(eventId, (k,v) -> v+1);
            counterEventGetByName.putIfAbsent(eventId, 1L);
        }
    }

    @AfterReturning(
            pointcut = "execution(* getTicketsPrice(..)) && args(eventId, ..) && withinBookingService())"
    )
    private void afterEventGetTicketPrice(Long eventId) {
        if (eventId != null) {
            counterEventGetPrice.computeIfPresent(eventId, (k,v) -> v+1);
            counterEventGetPrice.putIfAbsent(eventId, 1L);
        }
    }

    @AfterReturning(
            pointcut = "execution(* bookTickets(..)) && args(tickets) && withinBookingService())"
    )
    private void afterEventBookTickets(Set<Ticket> tickets) {
        Long eventId = tickets.iterator().next().getEventId();

        if (eventId != null) {
            counterEventBooked.computeIfPresent(eventId, (k,v) -> v+1);
            counterEventBooked.putIfAbsent(eventId, 1L);
        }
    }

}
