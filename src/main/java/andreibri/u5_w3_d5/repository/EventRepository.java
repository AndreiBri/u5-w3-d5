package andreibri.u5_w3_d5.repository;

import andreibri.u5_w3_d5.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

}
