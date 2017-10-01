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
import ua.epam.spring.hometask.dao.UserDAO;
import ua.epam.spring.hometask.domain.User;


import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@ContextConfiguration("classpath:spring.xml")
public class UserServiceTest {

    private static final User TEST_USER;

    static {
        TEST_USER = new User();
        TEST_USER.setId(1L);
        TEST_USER.setEmail("user@epam.com");
        TEST_USER.setFirstName("John");
        TEST_USER.setLastName("Coffey");
    }

    private static final User TEST_USER_2;

    static {
        TEST_USER_2 = new User();
        TEST_USER_2.setId(2L);
        TEST_USER_2.setEmail("user2@epam.com");
        TEST_USER_2.setFirstName("Alex");
        TEST_USER_2.setLastName("Row");
    }

    private static final Collection<User> TEST_USERS = Arrays.asList(
                TEST_USER,
                TEST_USER_2
    );

    @Mock
    private UserDAO userDAO;

    @Autowired
    @InjectMocks
    private UserService userService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFindUserByEmail() {
        when(userDAO.findByEmail(anyString())).thenReturn(TEST_USER);
        User user = userService.getUserByEmail(anyString());
        assertThat(user, is(equalTo(TEST_USER)));
        verify(userDAO, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userDAO);
    }

    @Test
    public void shouldUpdateUserTickets() {
        when(userDAO.updateTickets(anySet(), anyLong())).thenReturn(true);
        boolean result = userService.updateUserTickets(anySet(), anyLong());
        assertTrue(result);
        verify(userDAO, times(1)).updateTickets(anySet(), anyLong());
        verifyNoMoreInteractions(userDAO);
    }

    @Test
    public void shouldAddUser() {
        when(userDAO.add(TEST_USER)).thenReturn(TEST_USER);
        User user = userService.save(TEST_USER);
        assertThat(user, is(equalTo(TEST_USER)));
        verify(userDAO, times(1)).add(TEST_USER);
        verifyNoMoreInteractions(userDAO);
    }

    @Test
    public void shouldRemoveUser() {
        userService.remove(TEST_USER);
        verify(userDAO, times(1)).remove(TEST_USER);
        verifyNoMoreInteractions(userDAO);
    }

    @Test
    public void shouldFindUserById() {
        when(userDAO.getById(anyLong())).thenReturn(TEST_USER);
        User user = userService.getById(anyLong());
        assertThat(user, is(equalTo(TEST_USER)));
        verify(userDAO, times(1)).getById(anyLong());
        verifyNoMoreInteractions(userDAO);
    }

    @Test
    public void shouldFindAllUsers() {
        when(userDAO.getAll()).thenReturn(TEST_USERS);
        Collection<User> users = userService.getAll();
        assertNotNull(users);
        assertThat(users.size(), is(2));
        assertThat(users, is(equalTo(TEST_USERS)));
        verify(userDAO, times(1)).getAll();
        verifyNoMoreInteractions(userDAO);
    }
}
