package tourguide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tourguide.model.Room;
import tourguide.model.Tour;
import tourguide.model.User;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
//    @Query("SELECT r FROM Room r WHERE r.userId = :userId AND r.tourId = :tourId")
//    List<Room> findByUserIdAndTourId(Long userId, Long tourId);

    List<Room> findByRoomUser(User user);
    Optional<Room> findByRoomUserAndRoomTour(User user, Tour tour);
}

