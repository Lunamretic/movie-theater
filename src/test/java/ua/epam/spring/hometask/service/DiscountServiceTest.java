package ua.epam.spring.hometask.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;

import java.time.LocalDateTime;
import java.util.NavigableSet;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.temporal.TemporalAdjusters.next;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ContextConfiguration("classpath:spring.xml")
public class DiscountServiceTest {
    @Mock
    private NavigableSet<Ticket> tickets;

    private Event event = new Event();

    private LocalDateTime dateTime = LocalDateTime.now();

    @InjectMocks
    private User user = new User();

    @Autowired
    private DiscountService discountService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFindBestDiscount() {
        when(tickets.size()).thenReturn(3);
        double discount = discountService.getDiscount(user, event, dateTime.with(next(MONDAY)),
                9, 0, 2.0 );
        assertThat(discount, is(closeTo(5.555, 0.001)));
    }

    @Test
    public void shouldFindBestDiscount2() {
        when(tickets.size()).thenReturn(0);
        double discount = discountService.getDiscount(user, event, dateTime.with(next(MONDAY)),
                1, 0, 2.0 );
        assertThat(discount, is(equalTo(0.0)));
    }

    @Test
    public void shouldFindBestDiscount3() {
        when(tickets.size()).thenReturn(0);
        double discount = discountService.getDiscount(user, event, dateTime.with(next(TUESDAY)),
                1, 0, 2.0 );
        assertThat(discount, is(equalTo(5.0)));
    }

    @Test
    public void shouldFindBestDiscount4() {
        when(tickets.size()).thenReturn(10);
        double discount = discountService.getDiscount(user, event, dateTime.with(next(TUESDAY)),
                10, 0, 2.0 );
        assertThat(discount, is(equalTo(5.0)));
    }

    @Test
    public void shouldFindBestDiscount5() {
        when(tickets.size()).thenReturn(9);
        double discount = discountService.getDiscount(user, event, dateTime.with(next(TUESDAY)),
                1, 0, 2.0 );
        assertThat(discount, is(equalTo(50.0)));
    }

}
