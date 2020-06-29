package blogs.microservice.repository;

import blogs.microservice.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by manju on 28-06-2020.
 */
@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
}
