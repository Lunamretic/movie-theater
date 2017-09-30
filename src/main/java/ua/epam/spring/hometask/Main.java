package ua.epam.spring.hometask;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.epam.spring.hometask.service.AuditoriumService;
import ua.epam.spring.hometask.service.DiscountService;
import ua.epam.spring.hometask.service.UserService;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        AuditoriumService auditoriumService = (AuditoriumService) context.getBean("auditoriumService");
        auditoriumService.getAll().forEach(System.out::println);
    }
}
