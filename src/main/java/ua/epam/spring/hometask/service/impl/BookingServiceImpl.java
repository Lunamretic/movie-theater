package ua.epam.spring.hometask.service.impl;

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

    private double vipSeatMultiplier = 2.0;

    private double lowRatingMultiplier = 0.8;

    private double midRatingMultiplier = 1.0;

    private double highRatingMultiplier = 1.2;

    private DiscountService discountService;

    private UserService userService;

    private TicketDAO ticketDAO;

    public void setVipSeatMultiplier(double vipSeatMultiplier) {
        this.vipSeatMultiplier = vipSeatMultiplier;
    }

    public void setLowRatingMultiplier(double lowRatingMultiplier) {
        this.lowRatingMultiplier = lowRatingMultiplier;
    }

    public void setMidRatingMultiplier(double midRatingMultiplier) {
        this.midRatingMultiplier = midRatingMultiplier;
    }

    public void setHighRatingMultiplier(double highRatingMultiplier) {
        this.highRatingMultiplier = highRatingMultiplier;
    }

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
        totalPrice = event.getBasePrice() * (numRegularSeats + numVipSeats * vipSeatMultiplier);

        switch (event.getRating()) {
            case LOW:
                totalPrice *= lowRatingMultiplier;
                break;
            case MID:
                totalPrice *= midRatingMultiplier;
                break;
            case HIGH:
                totalPrice *= highRatingMultiplier;
                break;
            default:
                throw new IllegalArgumentException("Unknown rating for event");
        }

        double discount = discountService.getDiscount(user, event, dateTime,
                numRegularSeats, numVipSeats, vipSeatMultiplier);

        totalPrice -= totalPrice * discount / 100;

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
