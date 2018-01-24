package ua.epam.spring.hometask.service.discount.strategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * Discount for all tickets if the event takes place on Tuesday
 */
@Component
@PropertySource("classpath:/prop/application.properties")
public class EveryTuesdayStrategy implements DiscountStrategy {

    @Value("${discount.every-tuesday}")
    private int discountValue;

    public void setDiscountValue(byte discountValue) {
        this.discountValue = discountValue;
    }

    @Override
    public double calculateDiscount(@Nullable User user, @Nonnull Event event, @Nonnull LocalDateTime airDateTime,
                                    long numberOfRegularTickets, long numberOfVipTickets, double vipMultiplier) {

        return airDateTime.getDayOfWeek() == DayOfWeek.TUESDAY ? discountValue : 0;
    }

    @Override
    public String toString() {
        return "EveryTuesdayStrategy";
    }
}
