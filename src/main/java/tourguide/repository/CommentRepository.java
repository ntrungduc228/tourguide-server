package tourguide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourguide.model.Comment;
import tourguide.model.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdOrderByLastModifiedDateAsc(Long postId);
}
