package andreibri.u5_w3_d5.repository;

import andreibri.u5_w3_d5.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    boolean existsByUserAndEvent(UUID userId, UUID eventId);
}
