package tourguide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourguide.model.Appointment;
import tourguide.model.Tour;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByTour(Tour tour);
    List<Appointment> findByTourOrderByLastModifiedDateDesc(Tour tour);
    List<Appointment> findByTimeGreaterThan(LocalDateTime time);
}
