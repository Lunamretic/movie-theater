package ua.epam.spring.hometask;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.epam.spring.hometask.domain.*;
import ua.epam.spring.hometask.service.AuditoriumService;
import ua.epam.spring.hometask.service.BookingService;
import ua.epam.spring.hometask.service.EventService;
import ua.epam.spring.hometask.service.UserService;
import ua.epam.spring.hometask.service.impl.AuditoriumServiceImpl;
import ua.epam.spring.hometask.service.impl.BookingServiceImpl;
import ua.epam.spring.hometask.service.impl.EventServiceImpl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class AppTest {

    @Test
    public void appTest() {

        User user = new User();
        user.setEmail("user@epam.com");
        user.setFirstName("John");
        user.setLastName("Coffey");

        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        UserService userService = context.getBean(UserService.class);
        EventService eventService = context.getBean(EventServiceImpl.class);
        BookingService bookingService = context.getBean(BookingServiceImpl.class);
        AuditoriumService auditoriumService = context.getBean(AuditoriumServiceImpl.class);

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
        event.setBasePrice(600);
        event.setAirDates(airDates);
        event.setAuditoriums(auditoriums);

        eventService.save(event);
        System.out.println("Available events: ");
        eventService.getAll().forEach(System.out::println);

        System.out.println("Available auditoriums:");
        auditoriumService.getAll().forEach(System.out::println);

        Set<Long> seats = Stream.of(1L, 2L, 3L, 20L, 21L, 50L, 54L).collect(toSet());
        double price1 = bookingService.getTicketsPrice(event, firstDate, registeredUser, seats);
        System.out.println("1. Total price for tickets: " + price1);

        Set<Ticket> tickets = new HashSet<>();
        seats.forEach(ticket ->
                tickets.add(new Ticket(registeredUser, event, firstDate, ticket))
        );
        bookingService.bookTickets(tickets);

        bookingService.getPurchasedTicketsForEvent(event, firstDate).forEach(System.out::println);
    }

}
