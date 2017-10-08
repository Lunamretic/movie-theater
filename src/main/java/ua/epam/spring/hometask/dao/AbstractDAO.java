package ua.epam.spring.hometask.dao;

import ua.epam.spring.hometask.domain.DomainObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractDAO <T extends DomainObject> {

    protected static AtomicLong idCounter = new AtomicLong();

    protected Map<Long, T> domainObjectMap;

    {
        domainObjectMap = new HashMap<>();
    }

    public T add(T domainObject) {
        domainObject.setId(idCounter.getAndIncrement());
        domainObjectMap.put(domainObject.getId(), domainObject);
        return domainObject;
    }

    public void remove(T domainObject) {
        domainObjectMap.remove(domainObject.getId());
    }

    public T getById(Long id) {
        return domainObjectMap.get(id);
    }

    public Collection<T> getAll() {
        return domainObjectMap.values();
    }

}