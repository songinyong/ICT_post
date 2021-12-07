package post.domain.posts;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
    
    @Query("SELECT p FROM Posts p WHERE p.user_id =?1 ORDER BY p.id DESC ")
    List<Posts> findUsersPosts(int user_id);
    
    Page<Posts> findAll(Pageable pageable);
}