package ua.epam.spring.hometask;

import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ClassPathResource;
import ua.epam.spring.hometask.domain.Auditorium;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("ua.epam.spring.hometask")
public class AppConfig {

    @Resource(name = "propertiesFactoryBean")
    private LinkedHashMap<String, String> auditoriumPropMap;

    @Bean
    public PropertiesFactoryBean propertiesFactoryBean() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("prop/auditoriums.properties"));
        return bean;
    }

    @Bean
    public Set<Auditorium> auditoriums() {
        Set<Auditorium> auditoriums = new HashSet<>();

        int numAuditoriums = auditoriumPropMap.size() / 3;

        for (int i = 0; i < numAuditoriums; i++) {
            String name = auditoriumPropMap.get("auditorium" + i + ".name");

            long numberOfSeats = Long.parseLong(auditoriumPropMap.get("auditorium" + i + ".numberOfSeats"));

            Long[] vipSeats = (Long[]) ConvertUtils.convert(auditoriumPropMap.get("auditorium" + i + ".vipSeats"), Long[].class);
            Set<Long> vipSeatsSet = Arrays.stream(vipSeats).collect(Collectors.toSet());

            auditoriums.add(new Auditorium(name, numberOfSeats, vipSeatsSet));
        }

        return auditoriums;
    }
}
