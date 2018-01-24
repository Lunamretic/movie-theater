package ua.epam.spring.hometask.service.impl;

import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.service.AuditoriumService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.util.Set;

@Service
public class AuditoriumServiceImpl implements AuditoriumService {

    @Resource(name = "auditoriums")
    private Set<Auditorium> auditoriums;

    public @Nonnull Set<Auditorium> getAll() {
        return auditoriums;
    }

    public @Nullable Auditorium getByName(@Nonnull String name) {
        return auditoriums.stream().filter(auditorium -> auditorium.getName().contains(name)).findAny().orElse(null);
    }

}