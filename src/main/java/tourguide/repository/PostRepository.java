package tourguide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourguide.model.Post;
import tourguide.model.Tour;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> getByTourId(Long tourId);
}
