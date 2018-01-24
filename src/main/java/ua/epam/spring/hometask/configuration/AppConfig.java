package ua.epam.spring.hometask.configuration;

import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.epam.spring.hometask.domain.Auditorium;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@ComponentScan(basePackages = {
        "ua.epam.spring.hometask.dao",
        "ua.epam.spring.hometask.domain",
        "ua.epam.spring.hometask.service"})
public class AppConfig {

    @Resource(name = "propertiesFactoryBean")
    private LinkedHashMap<String, String> auditoriumPropMap;

    @Autowired
    DataSource dataSource;

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PropertiesFactoryBean propertiesFactoryBean() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("prop/auditoriums.properties"));
        return bean;
    }

    @Bean
    public Set<Auditorium> auditoriums() {
        Set<Auditorium> auditoriums = new HashSet<>();

        int numAuditoriums = auditoriumPropMap.size() / 3; //3 stands for the number of rows per each auditorium in .properties

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
