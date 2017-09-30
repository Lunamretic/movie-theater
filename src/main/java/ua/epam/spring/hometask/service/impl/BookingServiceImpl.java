package ua.epam.spring.hometask.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ua.epam.spring.hometask.dao.TicketDAO;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.BookingService;
import ua.epam.spring.hometask.service.DiscountService;
import ua.epam.spring.hometask.service.UserService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Set;

public class BookingServiceImpl implements BookingService {

    private static final double VIP_SEAT_MULTIPLIER = 2.0;

    private static final double LOW_RATING_MULTIPLIER = 0.8;

    private static final double HIGH_RATING_MULTIPLIER = 1.2;

    private DiscountService discountService;

    private UserService userService;

    private TicketDAO ticketDAO;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setDiscountService(DiscountService discountService) {
        this.discountService = discountService;
    }

    public void setTicketDAO(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    @Override
    public double getTicketsPrice(@Nonnull Event event, @Nonnull LocalDateTime dateTime, @Nullable User user, @Nonnull Set<Long> seats) {
        double totalPrice;

        long numVipSeats = event.getAuditoriums().get(dateTime).countVipSeats(seats);
        long numRegularSeats = seats.size() - numVipSeats;
        totalPrice = event.getBasePrice() * (numRegularSeats + numVipSeats * VIP_SEAT_MULTIPLIER);

        switch (event.getRating()) {
            case LOW:
                totalPrice *= LOW_RATING_MULTIPLIER;
                break;
            case MID:
                break;
            case HIGH:
                totalPrice *= HIGH_RATING_MULTIPLIER;
                break;
            default:
                throw new IllegalArgumentException("Unknown rating for event");
        }

        byte discount = discountService.getDiscount(user, event, dateTime, seats.size());
        totalPrice *= discount;

        return totalPrice;
    }

    @Override
    public void bookTickets(@Nonnull Set<Ticket> tickets) {
        ticketDAO.addAll(tickets);

        User user = tickets.iterator().next().getUser();
        if (user != null) {
            userService.updateUserTickets(tickets, user.getId());
        }
    }

    @Nonnull
    @Override
    public Set<Ticket> getPurchasedTicketsForEvent(@Nonnull Event event, @Nonnull LocalDateTime dateTime) {
        return ticketDAO.getTicketsForEvent(event, dateTime);
    }

}
