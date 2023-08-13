package tourguide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourguide.model.Destination;
import tourguide.model.Tour;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
    void deleteAllByTour(Tour tour);
}
