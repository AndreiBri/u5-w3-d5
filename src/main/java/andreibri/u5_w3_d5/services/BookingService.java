package andreibri.u5_w3_d5.services;

import andreibri.u5_w3_d5.dto.BookingResponse;
import andreibri.u5_w3_d5.entities.Booking;
import andreibri.u5_w3_d5.entities.Event;
import andreibri.u5_w3_d5.entities.User;
import andreibri.u5_w3_d5.exception.BadRequestException;
import andreibri.u5_w3_d5.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    public BookingResponse createBooking(UUID eventId, String username) {

        // Recupera utente ed evento dal DB
        User user = userService.getByUsername(username);
        Event event = eventService.getById(eventId);

        // Controlla che ci siano posti disponibili
        if (event.getAvailableSeats() <= 0) {
            throw new BadRequestException("No available seats for this event");
        }

        // Controlla che l'utente non abbia già prenotato questo evento
        if (bookingRepository.existsByUserAndEvent(user, event)) {
            throw new BadRequestException("User already has a booking for this event");
        }

        // Scala di 1 i posti disponibili
        event.setAvailableSeats(event.getAvailableSeats() - 1);
        eventService.save(event);

        // Crea la prenotazione con tutti i dati 
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setEvent(event);
        booking.setBookingDate(LocalDate.now());

        // Salva la prenotazione completa nel DB
        Booking saved = bookingRepository.save(booking);

        // Costruisce e ritorna la risposta
        BookingResponse res = new BookingResponse();
        res.id = saved.getId();
        res.eventId = event.getId();
        res.eventTitle = event.getTitle();
        res.username = user.getUsername();

        return res;
    }
}