package tourguide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourguide.model.Destination;
import tourguide.model.Tour;

import java.time.LocalDateTime;
import java.util.List;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
    void deleteAllByTour(Tour tour);
    List<Destination> findByDepartureTimeGreaterThan(LocalDateTime time);
}
