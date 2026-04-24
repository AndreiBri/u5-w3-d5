package andreibri.u5_w3_d5.dto;

import java.time.LocalDate;
import java.util.UUID;

public class EventResponse {
    public UUID id;
    public String title;
    public String description;
    public LocalDate date;
    public int availableSeats;
}
