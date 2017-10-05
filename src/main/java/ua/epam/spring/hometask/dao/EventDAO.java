package ua.epam.spring.hometask.dao;

import ua.epam.spring.hometask.domain.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class EventDAO extends AbstractDAO<Event> {

    @Nullable
    public Event findByName(@Nonnull String name) {
        return domainObjectMap.values().stream().filter(event -> event.getName().contains(name)).findFirst().orElse(null);
    }

}
