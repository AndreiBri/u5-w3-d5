package andreibri.u5_w3_d5.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Booking {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Event event;

    private LocalDate bookingDate;
}
