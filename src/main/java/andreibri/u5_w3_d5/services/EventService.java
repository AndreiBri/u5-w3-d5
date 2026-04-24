package andreibri.u5_w3_d5.services;

import andreibri.u5_w3_d5.dto.EventRequest;
import andreibri.u5_w3_d5.dto.EventResponse;
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

    public List<EventResponse> getAll() {
        return eventRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public EventResponse create(EventRequest req) {

        Event e = new Event();
        e.setTitle(req.title);
        e.setDescription(req.description);
        e.setDate(req.date);
        e.setAvailableSeats(req.availableSeats);

        Event saved = eventRepository.save(e);

        return toResponse(saved);
    }

    public EventResponse toResponse(Event e) {
        EventResponse res = new EventResponse();

        res.id = e.getId();
        res.title = e.getTitle();
        res.description = e.getDescription();
        res.date = e.getDate();
        res.availableSeats = e.getAvailableSeats();

        return res;
    }

    // Modifica un evento esistente
    public EventResponse update(UUID id, EventRequest req) {

        // Cerca l'evento nel DB, se non esiste lancia eccezione
        Event event = getById(id);

        // Aggiorna i campi con i nuovi valori
        event.setTitle(req.title);
        event.setDescription(req.description);
        event.setDate(req.date);
        event.setAvailableSeats(req.availableSeats);

        // Salva le modifiche
        Event saved = eventRepository.save(event);

        return toResponse(saved);
    }

    // Elimina un evento
    public void delete(UUID id) {

        // Controlla che l'evento esista prima di eliminarlo
        Event event = getById(id);

        eventRepository.delete(event);
    }
}
