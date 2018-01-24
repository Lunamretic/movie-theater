package ua.epam.spring.hometask.domain;

import java.io.Serializable;

/**
 * @author Yuriy_Tkach
 */
public class DomainObject implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
