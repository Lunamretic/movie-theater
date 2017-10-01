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
import ua.epam.spring.hometask.dao.EventDAO;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.EventRating;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ContextConfiguration("classpath:spring.xml")
public class EventServiceTest {

    @Mock
    private EventDAO eventDAO;

    @Autowired
    @InjectMocks
    private EventService eventService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private static final Event TEST_EVENT;

    static {
        TEST_EVENT = new Event();
        TEST_EVENT.setBasePrice(1.1);
        TEST_EVENT.setName("aaa");
        TEST_EVENT.setRating(EventRating.HIGH);
        LocalDateTime now = LocalDateTime.now();
        TEST_EVENT.addAirDateTime(now);
        TEST_EVENT.addAirDateTime(now.plusDays(1));
        TEST_EVENT.addAirDateTime(now.plusDays(2));
    }

    private static final Collection<Event> TEST_EVENTS = Arrays.asList(
            TEST_EVENT,
            TEST_EVENT
    );

    @Test
    public void shouldFindByName() {
        when(eventDAO.findByName(anyString())).thenReturn(TEST_EVENT);
        Event event = eventService.getByName(anyString());
        assertThat(event, is(equalTo(TEST_EVENT)));
        verify(eventDAO, times(1)).findByName(anyString());
        verifyNoMoreInteractions(eventDAO);
    }

    @Test
    public void shouldSaveEvent() {
        when(eventDAO.add(TEST_EVENT)).thenReturn(TEST_EVENT);
        Event event = eventService.save(TEST_EVENT);
        assertThat(event, is(equalTo(TEST_EVENT)));
        verify(eventDAO, times(1)).add(TEST_EVENT);
        verifyNoMoreInteractions(eventDAO);
    }

    @Test
    public void shouldRemoveEvent() {
        eventService.remove(TEST_EVENT);
        verify(eventDAO, times(1)).remove(TEST_EVENT);
        verifyNoMoreInteractions(eventDAO);
    }

    @Test
    public void shouldFindById() {
        when(eventDAO.getById(anyLong())).thenReturn(TEST_EVENT);
        Event event = eventService.getById(anyLong());
        assertThat(event, is(equalTo(TEST_EVENT)));
        verify(eventDAO, times(1)).getById(anyLong());
        verifyNoMoreInteractions(eventDAO);
    }

    @Test
    public void shouldFindAll() {
        when(eventDAO.getAll()).thenReturn(TEST_EVENTS);
        Collection<Event> events = eventService.getAll();
        assertThat(events, is(equalTo(TEST_EVENTS)));
        verify(eventDAO, times(1)).getAll();
        verifyNoMoreInteractions(eventDAO);
    }

}