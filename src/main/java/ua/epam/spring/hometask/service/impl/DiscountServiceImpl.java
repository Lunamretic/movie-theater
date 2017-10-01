package ua.epam.spring.hometask.service.impl;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.DiscountService;
import ua.epam.spring.hometask.service.discount.strategy.DiscountStrategy;

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
    public double getDiscount(@Nullable User user, @Nonnull Event event, @Nonnull LocalDateTime airDateTime,
                            long numberOfRegularTickets, long numberOfVipTickets, double vipMultiplier) {
        return strategies
                .stream()
                .map(discountStrategy -> discountStrategy.calculateDiscount(user, event, airDateTime,
                        numberOfRegularTickets, numberOfVipTickets, vipMultiplier))
                .max(Double::compare)
                .get();
    }

}
