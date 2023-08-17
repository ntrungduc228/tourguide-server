package tourguide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourguide.model.Tour;
import tourguide.model.User;

import java.util.List;
import java.util.Optional;

public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> findByIsProgress(Boolean isProgress);
}
