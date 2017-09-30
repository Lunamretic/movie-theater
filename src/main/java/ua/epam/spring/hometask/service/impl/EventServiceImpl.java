package ua.epam.spring.hometask.service.impl;

import ua.epam.spring.hometask.dao.EventDAO;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.service.AbstractDomainObjectService;
import ua.epam.spring.hometask.service.EventService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public class EventServiceImpl implements AbstractDomainObjectService<Event>, EventService {

    private EventDAO eventDAO;

    public void setEventDAO(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    @Nullable
    @Override
    public Event getByName(@Nonnull String name) {
        return eventDAO.findByName(name);
    }

    @Override
    public Event save(@Nonnull Event object) {
        return eventDAO.add(object);
    }

    @Override
    public void remove(@Nonnull Event object) {
        eventDAO.remove(object);
    }

    @Override
    public Event getById(@Nonnull Long id) {
        return eventDAO.getById(id);
    }

    @Nonnull
    @Override
    public Collection<Event> getAll() {
        return eventDAO.getAll();
    }

}
