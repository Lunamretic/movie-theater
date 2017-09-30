package ua.epam.spring.hometask.dao;

import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserDAO extends AbstractDAO<User> {

    private static Map<Long, User> userMap;

    static {
        userMap = new HashMap<>();
    }

    @Nullable
    public User findByEmail(@Nonnull String email) {
        return userMap.values().stream().filter(user -> user.getEmail().contains(email)).findFirst().orElse(null);
    }

    public boolean updateTickets(@Nonnull Set<Ticket> tickets, @Nonnull Long id) {
        return userMap.get(id).getTickets().addAll(tickets);
    }

    @Override
    public User add(User user) {
        return userMap.put(user.getId(), user);
    }

    @Override
    public void remove(User user) {
        userMap.remove(user.getId());
    }

    @Override
    public User getById(Long id) {
        return userMap.get(id);
    }

    @Override
    public Collection<User> getAll() {
        return userMap.values();
    }

}
