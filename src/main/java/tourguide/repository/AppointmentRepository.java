package tourguide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourguide.model.Appoinment;

public interface AppointmentRepository extends JpaRepository<Appoinment, Long> {
}
