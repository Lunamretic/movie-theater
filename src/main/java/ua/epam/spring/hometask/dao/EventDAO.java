package ua.epam.spring.hometask.dao;

import ua.epam.spring.hometask.domain.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EventDAO extends AbstractDAO<Event> {

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
        return eventMap.put(event.getId(), event);
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
