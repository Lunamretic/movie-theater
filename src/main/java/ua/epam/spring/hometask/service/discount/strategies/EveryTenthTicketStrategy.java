package ua.epam.spring.hometask.service.discount.strategies;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Component
@PropertySource ("classpath:/prop/discounts.properties")
public class EveryTenthTicketStrategy implements DiscountStrategy {

    private byte discountValue;

    public void setDiscountValue(byte discountValue) {
        this.discountValue = discountValue;
    }

    @Override
    public byte calculateDiscount(@Nullable User user, @Nonnull Event event, @Nonnull LocalDateTime airDateTime, long numberOfTickets) {
        byte discount;

        if (user != null) {
            long currentNumTickets = user.getTickets().size();
            discount = (currentNumTickets % 10 > (user.getTickets().size() + numberOfTickets) % 10) ? discountValue : 0;
        } else {
            discount = (numberOfTickets == 10) ? discountValue : 0;
        }
        return discount;
    }

}