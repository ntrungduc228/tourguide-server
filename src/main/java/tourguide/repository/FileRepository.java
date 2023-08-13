package tourguide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourguide.model.File;
import tourguide.model.Post;



public interface FileRepository extends JpaRepository<File, Long> {
    void deleteAllByPostFile(Post post);

}
