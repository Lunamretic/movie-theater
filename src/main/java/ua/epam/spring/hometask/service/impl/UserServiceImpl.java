package ua.epam.spring.hometask.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.dao.UserDAO;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.UserService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Nullable
    @Override
    public User getUserByEmail(@Nonnull String email) {
        return userDAO.findByEmail(email);
    }

    @Override
    public boolean updateUserTickets(@Nonnull Set<Ticket> tickets, @Nonnull Long id) {
        return userDAO.updateTickets(tickets, id);
    }

    @Override
    public User save(@Nonnull User object) {
        return userDAO.add(object);
    }

    @Override
    public void remove(@Nonnull User object) {
        userDAO.remove(object);
    }

    @Override
    public User getById(@Nonnull Long id) {
        return userDAO.getById(id);
    }

    @Nonnull
    @Override
    public Collection<User> getAll() {
        return userDAO.getAll();
    }

}
