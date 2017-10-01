package ua.epam.spring.hometask.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import ua.epam.spring.hometask.domain.Auditorium;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ContextConfiguration("classpath:spring.xml")
public class AuditoriumServiceTest {

    private static final Auditorium TEST_AUDITORIUM;

    static {
        TEST_AUDITORIUM = new Auditorium();
        TEST_AUDITORIUM.setName("1b");
        TEST_AUDITORIUM.setNumberOfSeats(80);
        TEST_AUDITORIUM.setVipSeats(Stream.of(1L, 2L, 3L, 4L, 5L).collect(toSet()));
    }

    @Autowired
    private AuditoriumService auditoriumService;

    @Test
    public void shouldFindAllAuditoriums() {
        Set<Auditorium> auditoriums = auditoriumService.getAll();
        assertNotNull(auditoriums);
        assertThat(auditoriums.size(), is(equalTo(4)));
    }

    @Test
    public void shouldFindByName() {
        Auditorium auditorium = auditoriumService.getByName("1b");
        assertThat(auditorium, is(equalTo(TEST_AUDITORIUM)));
    }

}