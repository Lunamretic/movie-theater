package ua.epam.spring.hometask.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Ticket;

import java.util.Set;

@Aspect
@Component
@PropertySource("classpath:/prop/application.properties")
public class LuckyWinnerAspect {

    @Value("${luckyWinnerMagicNumber}")
    private Long luckyWinnerMagicNumber;

    private boolean isLuckyUser() {
        long millis = System.currentTimeMillis();
        return millis % luckyWinnerMagicNumber == 0;
    }

    @Before(value = "execution(* bookTickets(..)) && args(tickets) && target(ua.epam.spring.hometask.service.BookingService)")
    private void checkWhenEventBookTickets(Set<Ticket> tickets) {
        if (isLuckyUser()) {
            System.out.println("Lucky user! The price for all tickets is now zero!");
        }
    }
}
