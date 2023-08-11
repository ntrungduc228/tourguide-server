package tourguide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourguide.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByPhoneStartingWith(String prefix);
}
