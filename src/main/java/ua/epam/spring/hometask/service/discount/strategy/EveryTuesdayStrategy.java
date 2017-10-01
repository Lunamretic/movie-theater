package ua.epam.spring.hometask.service.discount.strategy;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * Discount for all tickets if the event takes place on Tuesday
 */
public class EveryTuesdayStrategy implements DiscountStrategy {

    private byte discountValue;

    public void setDiscountValue(byte discountValue) {
        this.discountValue = discountValue;
    }

    @Override
    public double calculateDiscount(@Nullable User user, @Nonnull Event event, @Nonnull LocalDateTime airDateTime,
                                    long numberOfRegularTickets, long numberOfVipTickets, double vipMultiplier) {

        return airDateTime.getDayOfWeek() == DayOfWeek.TUESDAY ? discountValue : 0;
    }

}
