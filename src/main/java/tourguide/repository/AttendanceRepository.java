package tourguide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourguide.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}
