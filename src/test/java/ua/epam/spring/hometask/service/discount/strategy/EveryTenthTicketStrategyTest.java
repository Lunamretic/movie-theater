package ua.epam.spring.hometask.service.discount.strategy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;

import java.time.LocalDateTime;
import java.util.NavigableSet;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.mockito.Mockito.when;

public class EveryTenthTicketStrategyTest {

    @Mock
    private NavigableSet<Ticket> tickets;

    @InjectMocks
    private User user = new User();

    private Event event = new Event();

    private static final LocalDateTime DATE_TIME = LocalDateTime.now().plusDays(1);

    private static final double VIP_SEAT_MULTIPLIER = 2.0;

    private EveryTenthTicketStrategy everyTenthTicketStrategy = new EveryTenthTicketStrategy();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCalculateDiscount1() {
        when(tickets.size()).thenReturn(0);
        everyTenthTicketStrategy.setDiscountValue((byte) 50);
        double discount = everyTenthTicketStrategy.calculateDiscount(user, event, DATE_TIME,
                12, 3, VIP_SEAT_MULTIPLIER);
        assertThat(discount, is(closeTo(2.777, 0.001)));
    }

    @Test
    public void shouldCalculateDiscount2() {
        when(tickets.size()).thenReturn(0);
        everyTenthTicketStrategy.setDiscountValue((byte) 50);
        double discount = everyTenthTicketStrategy.calculateDiscount(user, event, DATE_TIME,
                20, 10, VIP_SEAT_MULTIPLIER);
        assertThat(discount, is(closeTo(3.75, 0.001)));
    }

    @Test
    public void shouldCalculateDiscount3() {
        when(tickets.size()).thenReturn(0);
        everyTenthTicketStrategy.setDiscountValue((byte) 100);
        double discount = everyTenthTicketStrategy.calculateDiscount(user, event, DATE_TIME,
                10, 0, VIP_SEAT_MULTIPLIER);
        assertThat(discount, is(closeTo(10, 0.001)));
    }

    @Test
    public void shouldCalculateDiscount4() {
        when(tickets.size()).thenReturn(0);
        everyTenthTicketStrategy.setDiscountValue((byte) 100);
        double discount = everyTenthTicketStrategy.calculateDiscount(user, event, DATE_TIME,
                0, 10, VIP_SEAT_MULTIPLIER);
        assertThat(discount, is(closeTo(0, 0.01)));
    }

    @Test
    public void shouldCalculateDiscount5() {
        when(tickets.size()).thenReturn(19);
        everyTenthTicketStrategy.setDiscountValue((byte) 50);
        double discount = everyTenthTicketStrategy.calculateDiscount(user, event, DATE_TIME,
                7, 2, VIP_SEAT_MULTIPLIER);
        assertThat(discount, is(closeTo(4.545, 0.001)));
    }

    @Test
    public void shouldCalculateDiscount6() {
        when(tickets.size()).thenReturn(19);
        everyTenthTicketStrategy.setDiscountValue((byte) 50);
        double discount = everyTenthTicketStrategy.calculateDiscount(user, event, DATE_TIME,
                0, 2, VIP_SEAT_MULTIPLIER);
        assertThat(discount, is(equalTo(0.0)));
    }

    @Test
    public void shouldCalculateDiscount7() {
        when(tickets.size()).thenReturn(0);
        everyTenthTicketStrategy.setDiscountValue((byte) 50);
        double discount = everyTenthTicketStrategy.calculateDiscount(user, event, DATE_TIME,
                1, 19, VIP_SEAT_MULTIPLIER);
        assertThat(discount, is(closeTo(1.282, 0.001)));
    }

}
