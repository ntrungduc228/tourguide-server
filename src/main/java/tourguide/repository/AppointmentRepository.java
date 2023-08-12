package tourguide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourguide.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
