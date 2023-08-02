package tourguide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourguide.model.Destination;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
}
