package example.rest.repo;

import example.rest.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment,Long> {


}
