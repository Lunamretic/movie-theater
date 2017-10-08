package ua.epam.spring.hometask.dao;

import org.springframework.stereotype.Repository;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

@Repository
public class UserDAO extends AbstractDAO<User> {

    @Nullable
    public User findByEmail(@Nonnull String email) {
        return domainObjectMap.values().stream().filter(user -> user.getEmail().contains(email)).findFirst().orElse(null);
    }

    public boolean updateTickets(@Nonnull Set<Ticket> tickets, @Nonnull Long id) {
        return domainObjectMap.get(id).getTickets().addAll(tickets);
    }

}
