package tourguide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourguide.model.Appointment;
import tourguide.model.Attendance;
import tourguide.model.Destination;
import tourguide.model.User;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByUserAndAppointment(User user, Appointment appointment);
}
