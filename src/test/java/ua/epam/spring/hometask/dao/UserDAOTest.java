package ua.epam.spring.hometask.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import ua.epam.spring.hometask.configuration.AppConfig;
import ua.epam.spring.hometask.configuration.DBConfig;
import ua.epam.spring.hometask.domain.User;

import static junit.framework.TestCase.assertNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class
})
@ContextConfiguration(classes = {AppConfig.class, DBConfig.class})
public class UserDAOTest {

    /*
     * I realized that to accomplish this task, I need to change a lot of what was done in the previous tasks.
     * Unfortunately I do not have time for this.
     * Perhaps the skeleton of the task is too confusing and requires some changes.
     *
     * Just a little demonstration of db work in this class.
     */

    @Autowired
    private UserDAO userDAO;

    @Test
    public void userDAOTest() {
        User user1 = new User();
        user1.setFirstName("user1");
        user1.setLastName("user2");
        user1.setEmail("user2@gmail.com");
        User userAfterInsert = userDAO.add(user1);
        assertThat(userAfterInsert.getId(), is(equalTo(1L)));
        user1.setId(1L);

        User user2 = new User();
        user2.setFirstName("user2");
        user2.setLastName("user2");
        user2.setEmail("user2@gmail.com");
        userDAO.add(user2);
        user2.setId(2L);

        User userGetById = userDAO.getById(1L);
        assertThat(userGetById, is(equalTo(user1)));

        userDAO.remove(user1);
        User userAfterDelete = userDAO.getById(1L);
        assertNull(userAfterDelete);


    }

}
