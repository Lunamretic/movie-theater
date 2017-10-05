package ua.epam.spring.hometask.dao;

import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class UserDAO extends AbstractDAO<User> {

    @Nullable
    public User findByEmail(@Nonnull String email) {
        return domainObjectMap.values().stream().filter(user -> user.getEmail().contains(email)).findFirst().orElse(null);
    }

    public boolean updateTickets(@Nonnull Set<Ticket> tickets, @Nonnull Long id) {
        return domainObjectMap.get(id).getTickets().addAll(tickets);
    }

}
