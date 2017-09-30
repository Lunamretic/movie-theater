package ua.epam.spring.hometask.dao;

import ua.epam.spring.hometask.domain.DomainObject;

import java.util.Collection;
import java.util.Map;

public abstract class AbstractDAO <T extends DomainObject> {

    public abstract T add(T domainObject);

    public abstract void remove(T domainObject);

    public abstract T getById(Long id);

    public abstract Collection<T> getAll();
}
