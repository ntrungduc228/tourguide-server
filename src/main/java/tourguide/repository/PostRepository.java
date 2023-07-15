package tourguide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourguide.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
