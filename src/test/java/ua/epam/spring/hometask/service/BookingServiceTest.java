package ua.epam.spring.hometask.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import ua.epam.spring.hometask.dao.TicketDAO;
import ua.epam.spring.hometask.domain.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ContextConfiguration("classpath:spring.xml")
public class BookingServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Event event;

    @Mock
    private Auditorium auditorium;

    private User user = new User();

    @Mock
    private Set<Long> seats;

    @Mock
    private DiscountService discountService;

    @Mock
    private UserService userService;

    @Mock
    private TicketDAO ticketDAO;

    private LocalDateTime dateTime = LocalDateTime.now();

    private static final User TEST_USER;

    static {
        TEST_USER = new User();
        TEST_USER.setId(1L);
    }

    private static final Set<Ticket> TEST_TICKETS = Stream.of(
            new Ticket(TEST_USER, new Event(), LocalDateTime.now(), 1),
            new Ticket(TEST_USER, new Event(), LocalDateTime.now(), 2),
            new Ticket(TEST_USER, new Event(), LocalDateTime.now(), 3))
            .collect(Collectors.toSet());

    private static final Set<Ticket> TEST_TICKETS2 = Stream.of(
            new Ticket(null, new Event(), LocalDateTime.now(), 1),
            new Ticket(null, new Event(), LocalDateTime.now(), 2),
            new Ticket(null, new Event(), LocalDateTime.now(), 3))
            .collect(Collectors.toSet());

    @Autowired
    @InjectMocks
    private BookingService bookingService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(event.getAuditoriums().get(dateTime)).thenReturn(auditorium);
        when(auditorium.countVipSeats(seats)).thenReturn(0L);
        when(event.getBasePrice()).thenReturn(100.0);
    }

    @Test
    public void shouldCalculatePriceWithBestDiscount() {
        when(event.getRating()).thenReturn(EventRating.MID);
        when(seats.size()).thenReturn(1);
        when(discountService.getDiscount(any(), any(), any(), anyLong(), anyLong(), anyDouble())).thenReturn(5.0);

        double totalPrice = bookingService.getTicketsPrice(event, dateTime, user, seats);

        assertThat(totalPrice, is(closeTo(95.0, 0.001)));
    }

    @Test
    public void shouldCalculatePriceWithBestDiscount2() {
        when(event.getRating()).thenReturn(EventRating.LOW);
        when(seats.size()).thenReturn(1);
        when(discountService.getDiscount(any(), any(), any(), anyLong(), anyLong(), anyDouble())).thenReturn(5.0);

        double totalPrice = bookingService.getTicketsPrice(event, dateTime, user, seats);

        assertThat(totalPrice, is(closeTo(76.0, 0.001)));
    }

    @Test
    public void shouldCalculatePriceWithBestDiscount3() {
        when(event.getRating()).thenReturn(EventRating.HIGH);
        when(seats.size()).thenReturn(1);
        when(discountService.getDiscount(any(), any(), any(), anyLong(), anyLong(), anyDouble())).thenReturn(10.0);

        double totalPrice = bookingService.getTicketsPrice(event, dateTime, user, seats);

        assertThat(totalPrice, is(closeTo(108.0, 0.001)));
    }

    @Test
    public void shouldCalculatePriceWithBestDiscount4() {
        when(auditorium.countVipSeats(seats)).thenReturn(1L);
        when(event.getRating()).thenReturn(EventRating.HIGH);
        when(seats.size()).thenReturn(2);
        when(discountService.getDiscount(any(), any(), any(), anyLong(), anyLong(), anyDouble())).thenReturn(10.0);

        double totalPrice = bookingService.getTicketsPrice(event, dateTime, user, seats);

        assertThat(totalPrice, is(closeTo(324.0, 0.001)));
    }

    @Test
    public void shouldBookTickets() {
        bookingService.bookTickets(TEST_TICKETS);
        verify(ticketDAO, times(1)).addAll(TEST_TICKETS);
        verify(userService, times(1)).updateUserTickets(TEST_TICKETS, 1L);
        verifyNoMoreInteractions(ticketDAO);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldBookTickets2() {
        bookingService.bookTickets(TEST_TICKETS2);
        verify(ticketDAO, times(1)).addAll(TEST_TICKETS2);
        verify(userService, times(0)).updateUserTickets(TEST_TICKETS2, 1L);
        verifyNoMoreInteractions(ticketDAO);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldGetPurchasedTicketsForEvent() {
        when(ticketDAO.getTicketsForEvent(any(), any())).thenReturn(TEST_TICKETS);
        Set<Ticket> tickets = bookingService.getPurchasedTicketsForEvent(new Event(), dateTime);
        assertNotNull(tickets);
        assertThat(tickets.size(), is(equalTo(3)));
        verify(ticketDAO, times(1)).getTicketsForEvent(any(), any());
        verifyNoMoreInteractions(ticketDAO);
    }



}