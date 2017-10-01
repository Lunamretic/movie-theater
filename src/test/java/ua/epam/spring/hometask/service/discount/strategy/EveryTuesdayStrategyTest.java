package ua.epam.spring.hometask.service.discount.strategy;

import org.junit.Test;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;

import java.time.LocalDateTime;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.temporal.TemporalAdjusters.next;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;

public class EveryTuesdayStrategyTest {

    private User user = new User();

    private Event event = new Event();

    private EveryTuesdayStrategy everyTuesdayStrategy = new EveryTuesdayStrategy();

    @Test
    public void shouldCalculateDiscountForTuesday() {
        everyTuesdayStrategy.setDiscountValue((byte) 5);
        LocalDateTime dateTime = LocalDateTime.now().with(next(TUESDAY));
        double discount = everyTuesdayStrategy.calculateDiscount(user, event, dateTime,
                2, 1, 2.0);
        assertThat(discount, is(closeTo(5, 0.001)));
    }

    @Test
    public void shouldNotCalculateDiscountForFriday() {
        everyTuesdayStrategy.setDiscountValue((byte) 5);
        LocalDateTime dateTime = LocalDateTime.now().with(next(FRIDAY));
        double discount = everyTuesdayStrategy.calculateDiscount(user, event, dateTime,
                2, 1, 2.0);
        assertThat(discount, is(closeTo(0, 0.001)));
    }

}