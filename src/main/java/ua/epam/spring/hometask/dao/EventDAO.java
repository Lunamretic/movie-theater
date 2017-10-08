package ua.epam.spring.hometask.dao;

import org.springframework.stereotype.Repository;
import ua.epam.spring.hometask.domain.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Repository
public class EventDAO extends AbstractDAO<Event> {

    @Nullable
    public Event findByName(@Nonnull String name) {
        return domainObjectMap.values().stream().filter(event -> event.getName().contains(name)).findFirst().orElse(null);
    }

}
