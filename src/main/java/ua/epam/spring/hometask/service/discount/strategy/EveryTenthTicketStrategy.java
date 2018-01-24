package ua.epam.spring.hometask.service.discount.strategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;

/**
 * Discount for every 10th ticket. Can be applied only to regular tickets.
 */
@Component
@PropertySource("classpath:/prop/application.properties")
public class EveryTenthTicketStrategy implements DiscountStrategy {

    @Value("${discount.every-tenth}")
    private int discountValue;

    public void setDiscountValue(byte discountValue) {
        this.discountValue = discountValue;
    }

    @Override
    public double calculateDiscount(@Nullable User user, @Nonnull Event event, @Nonnull LocalDateTime airDateTime,
                                  long numberOfRegularTickets, long numberOfVipTickets, double vipMultiplier) {

        if (numberOfRegularTickets == 0) {
            return 0;
        }

        double discount;

        long currentNumTickets = (user != null) ? user.getTickets().size() : 0;

        long discountCount = (currentNumTickets % 10 + numberOfRegularTickets + numberOfVipTickets) / 10;

        if (discountCount == 0) {
            return 0;
        }

        if (discountCount > numberOfRegularTickets) {
            discountCount = numberOfRegularTickets;
        }

        double valueOfVipTickets = numberOfVipTickets * vipMultiplier;
        double valueOfAllTickets = numberOfRegularTickets + valueOfVipTickets;
        double valueOfDiscountTickets = (double) discountCount * discountValue / 100;

        discount = 100 - (valueOfAllTickets - valueOfDiscountTickets) * 100 / valueOfAllTickets;

        return discount;
    }

    @Override
    public String toString() {
        return "EveryTenthTicketStrategy";
    }
}