package ua.epam.spring.hometask;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import ua.epam.spring.hometask.domain.*;
import ua.epam.spring.hometask.service.AuditoriumService;
import ua.epam.spring.hometask.service.BookingService;
import ua.epam.spring.hometask.service.EventService;
import ua.epam.spring.hometask.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ContextConfiguration(classes = AppConfig.class)
public class AppTest {

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private AuditoriumService auditoriumService;

    @Test
    public void appTest() {

        User user = new User();
        user.setEmail("user@epam.com");
        user.setFirstName("John");
        user.setLastName("Coffey");

        User registeredUser = userService.save(user);

        System.out.println("User registered: " + registeredUser.toString());

        LocalDateTime firstDate = LocalDateTime.now().plusDays(5);
        LocalDateTime secondDate = LocalDateTime.now().plusDays(7);

        NavigableSet<LocalDateTime> airDates = new TreeSet<>();
        airDates.add(firstDate);
        airDates.add(secondDate);
        NavigableMap<LocalDateTime, Auditorium> auditoriums = new TreeMap<>();
        auditoriums.put(firstDate, auditoriumService.getByName("1a"));
        auditoriums.put(secondDate, auditoriumService.getByName("1b"));

        Event event = new Event();
        event.setId(10L);
        event.setName("Akira Yamaoka Concert");
        event.setRating(EventRating.HIGH);
        event.setBasePrice(100);
        event.setAirDates(airDates);
        event.setAuditoriums(auditoriums);

        eventService.save(event);
        System.out.println("Available events: ");
        eventService.getAll().forEach(System.out::println);

        System.out.println("Available auditoriums:");
        auditoriumService.getAll().forEach(System.out::println);

        Set<Long> seats1 = Stream.of(1L, 2L, 3L, 20L, 21L, 50L, 54L).collect(toSet());
        double price1 = bookingService.getTicketsPrice(event, firstDate, registeredUser, seats1);
        System.out.println("1. Total price for tickets: " + price1); // 1.2 ( 100 * 2 * 2 + 100 * 5) - 0

        Set<Ticket> tickets1 = new HashSet<>();
        seats1.forEach(ticket ->
                tickets1.add(new Ticket(registeredUser, event, firstDate, ticket))
        );
        bookingService.bookTickets(tickets1);

        bookingService.getPurchasedTicketsForEvent(event, firstDate).forEach(System.out::println);

        Set<Long> seats2 = Stream.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L).collect(toSet());
        double price2 = bookingService.getTicketsPrice(event, secondDate, registeredUser, seats2);
        System.out.println("2. Total price for tickets: " + price2); // 1.2 ( 100 * 5 * 2 + 100 * 5) - 100 * 0.5 * 1.2

        Set<Ticket> tickets2 = new HashSet<>();
        seats2.forEach(ticket ->
                tickets2.add(new Ticket(registeredUser, event, secondDate, ticket))
        );
        bookingService.bookTickets(tickets2);

        bookingService.getPurchasedTicketsForEvent(event, secondDate).forEach(System.out::println);
    }

}
