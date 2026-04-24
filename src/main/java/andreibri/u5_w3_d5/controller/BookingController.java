package andreibri.u5_w3_d5.controller;

import andreibri.u5_w3_d5.dto.BookingRequest;
import andreibri.u5_w3_d5.dto.BookingResponse;
import andreibri.u5_w3_d5.services.BookingService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping
    public BookingResponse create(@RequestBody BookingRequest req) {

        // Prende lo username dell'utente loggato dal token JWT
        String username = Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .getName();

        // Ritorna la risposta con i dettagli della prenotazione
        return service.createBooking(req.eventId, username);
    }
}