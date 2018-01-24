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
import ua.epam.spring.hometask.configuration.AppConfig;
import ua.epam.spring.hometask.configuration.DBConfig;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.discount.strategy.EveryTenthTicketStrategy;
import ua.epam.spring.hometask.service.discount.strategy.EveryTuesdayStrategy;
import ua.epam.spring.hometask.service.impl.DiscountServiceImpl;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ContextConfiguration(classes = {AppConfig.class, DBConfig.class})

public class DiscountServiceTest {

    @Mock
    private EveryTenthTicketStrategy everyTenthTicketStrategy;

    @Mock
    private EveryTuesdayStrategy everyTuesdayStrategy;

    @Autowired
    @InjectMocks
    private DiscountServiceImpl discountService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        discountService.setStrategies(Stream.of(everyTenthTicketStrategy, everyTuesdayStrategy).collect(Collectors.toList()));
    }

    @Test
    public void shouldChooseBestDiscount1() {
        when(everyTenthTicketStrategy.calculateDiscount(any(User.class), any(Event.class), any(LocalDateTime.class), anyLong(), anyLong(), anyDouble())).thenReturn(10.0);
        when(everyTuesdayStrategy.calculateDiscount(any(User.class), any(Event.class), any(LocalDateTime.class), anyLong(), anyLong(), anyDouble())).thenReturn(20.0);

        double discount = discountService.getDiscount(new User(), new Event(), LocalDateTime.now(), 0, 0, 0.0);

        assertThat(discount, is(equalTo(20.0)));
    }

    @Test
    public void shouldChooseBestDiscount2() {
        when(everyTenthTicketStrategy.calculateDiscount(any(User.class), any(Event.class), any(LocalDateTime.class), anyLong(), anyLong(), anyDouble())).thenReturn(20.0);
        when(everyTuesdayStrategy.calculateDiscount(any(User.class), any(Event.class), any(LocalDateTime.class), anyLong(), anyLong(), anyDouble())).thenReturn(10.0);

        double discount = discountService.getDiscount(new User(), new Event(), LocalDateTime.now(), 0, 0, 0.0);

        assertThat(discount, is(equalTo(20.0)));
    }

}
