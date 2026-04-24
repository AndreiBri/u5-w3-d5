package andreibri.u5_w3_d5.services;

import andreibri.u5_w3_d5.dto.EventRequest;
import andreibri.u5_w3_d5.entities.Event;
import andreibri.u5_w3_d5.exception.NotFoundException;
import andreibri.u5_w3_d5.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event getById(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
    }

    public void save(Event e) {
        eventRepository.save(e);
    }

    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    public Event create(EventRequest req) {
        Event e = new Event();
        e.setTitle(req.title);
        e.setDescription(req.description);
        e.setDate(req.date);
        e.setAvailableSeats(req.availableSeats);
        return eventRepository.save(e);
    }
}
