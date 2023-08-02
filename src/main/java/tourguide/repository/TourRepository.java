package tourguide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourguide.model.Tour;

public interface TourRepository extends JpaRepository<Tour, Long> {
}
