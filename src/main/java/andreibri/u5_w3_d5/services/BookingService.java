package andreibri.u5_w3_d5.services;

import andreibri.u5_w3_d5.entities.Booking;
import andreibri.u5_w3_d5.entities.Event;
import andreibri.u5_w3_d5.entities.User;
import andreibri.u5_w3_d5.exception.BadRequestException;
import andreibri.u5_w3_d5.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Transactional
    public Booking createBooking(UUID eventId, String username) {

        User user = userService.getByUsername(username);
        Event event = eventService.getById(eventId);

        if (event.getAvailableSeats() <= 0) {
            throw new BadRequestException("No available seats for event");
        }

        if (bookingRepository.existsByUserAndEvent(user, event)) {
            throw new BadRequestException("User already has a booking for this event");
        }

        event.setAvailableSeats(event.getAvailableSeats() - 1);
        eventService.save(event);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setEvent(event);

        return bookingRepository.save(booking);
    }
}
