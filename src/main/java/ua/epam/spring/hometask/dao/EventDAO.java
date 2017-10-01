package ua.epam.spring.hometask.dao;

import ua.epam.spring.hometask.domain.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class EventDAO extends AbstractDAO<Event> {

    private static AtomicLong idCounter = new AtomicLong();

    private static Map<Long, Event> eventMap;

    static {
        eventMap = new HashMap<>();
    }

    @Nullable
    public Event findByName(@Nonnull String name) {
        return eventMap.values().stream().filter(event -> event.getName().contains(name)).findFirst().orElse(null);
    }

    @Override
    public Event add(Event event) {
        event.setId(idCounter.getAndIncrement());
        eventMap.put(event.getId(), event);
        return event;
    }

    @Override
    public void remove(Event event) {
        eventMap.remove(event.getId());
    }

    @Override
    public Event getById(Long id) {
        return eventMap.get(id);
    }

    @Override
    public Collection<Event> getAll() {
        return eventMap.values();
    }
}
