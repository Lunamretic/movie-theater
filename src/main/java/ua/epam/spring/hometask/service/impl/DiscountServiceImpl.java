package ua.epam.spring.hometask.service.impl;

import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.DiscountService;
import ua.epam.spring.hometask.service.discount.strategies.DiscountStrategy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.List;


public class DiscountServiceImpl implements DiscountService {

    private List<DiscountStrategy> strategies;

    public void setStrategies(List<DiscountStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public byte getDiscount(@Nullable User user, @Nonnull Event event, @Nonnull LocalDateTime airDateTime, long numberOfTickets) {
        //List<Byte> discounts = new ArrayList<>();
        //strategies.forEach(discountStrategy -> discounts.add(discountStrategy.calculateDiscount(user, event, airDateTime, numberOfTickets)));
        return strategies
                .stream()
                .map(discountStrategy -> discountStrategy.calculateDiscount(user, event, airDateTime, numberOfTickets))
                .max(Integer::compare)
                .get();
    }

}
