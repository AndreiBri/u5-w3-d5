package andreibri.u5_w3_d5.controller;

import andreibri.u5_w3_d5.dto.EventRequest;
import andreibri.u5_w3_d5.dto.EventResponse;
import andreibri.u5_w3_d5.services.EventService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    // Solo ORGANIZER può creare eventi
    @PreAuthorize("hasRole('ORGANIZER')")
    @PostMapping
    public EventResponse create(@RequestBody EventRequest req) {
        return service.create(req);
    }

    // Tutti gli utenti loggati possono vedere gli eventi
    @GetMapping
    public List<EventResponse> getAll() {
        return service.getAll();
    }

    // Solo ORGANIZER può modificare eventi
    @PreAuthorize("hasRole('ORGANIZER')")
    @PutMapping("/{id}")
    public EventResponse update(@PathVariable UUID id, @RequestBody EventRequest req) {
        return service.update(id, req);
    }

    // Solo ORGANIZER può eliminare eventi
    @PreAuthorize("hasRole('ORGANIZER')")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable UUID id) {
        service.delete(id);
        return "Evento eliminato con successo";
    }
}