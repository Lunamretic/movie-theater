package ua.epam.spring.hometask.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
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

@Service
@PropertySource("classpath:/prop/application.properties")
public class BookingServiceImpl implements BookingService {

    @Value("${multiplier.vip-seat}")
    private double vipSeatMultiplier;

    @Value("${multiplier.low-rating}")
    private double lowRatingMultiplier;

    @Value("${multiplier.mid-rating}")
    private double midRatingMultiplier;

    @Value("${multiplier.high-rating}")
    private double highRatingMultiplier;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketDAO ticketDAO;

    @Override
    public double getTicketsPrice(@Nonnull Event event, @Nonnull LocalDateTime dateTime, @Nullable User user, @Nonnull Set<Long> seats) {

        double totalPrice;

        long numVipSeats = event.getAuditoriums().get(dateTime).countVipSeats(seats);
        long numRegularSeats = seats.size() - numVipSeats;

        double discount = discountService.getDiscount(user, event, dateTime,
                numRegularSeats, numVipSeats, vipSeatMultiplier); //vipSeatMultiplier is necessary for calculating the "value" of VIP tickets

        totalPrice = event.getBasePrice() * (numRegularSeats + numVipSeats * vipSeatMultiplier);

        totalPrice -= totalPrice * discount / 100;

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

        return totalPrice;
    }

    @Override
    public void bookTickets(@Nonnull Set<Ticket> tickets) {
        ticketDAO.addAll(tickets);

        Long userId = tickets.iterator().next().getUserId();
        if (userId != null) {
            //FIXME userService.updateUserTickets(tickets, userId);
        }
    }

    @Nonnull
    @Override
    public Set<Ticket> getPurchasedTicketsForEvent(@Nonnull Long eventId, @Nonnull LocalDateTime dateTime) {
        return ticketDAO.getTicketsForEvent(eventId, dateTime);
    }

}
