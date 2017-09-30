package ua.epam.spring.hometask.service.impl;

import ua.epam.spring.hometask.dao.UserDAO;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.AbstractDomainObjectService;
import ua.epam.spring.hometask.service.UserService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;

public class UserServiceImpl implements AbstractDomainObjectService<User>, UserService {

    private UserDAO userDAO;

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Nullable
    @Override
    public User getUserByEmail(@Nonnull String email) {
        return null;
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
