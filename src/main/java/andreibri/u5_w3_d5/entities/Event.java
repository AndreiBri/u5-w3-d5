package andreibri.u5_w3_d5.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Event {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private String description;
    private LocalDate date;
    private String location;

    private int totalSeats;
    private int availableSeats;

    @ManyToOne
    private User organizer;
}
