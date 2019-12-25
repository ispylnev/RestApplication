package example.rest.repo;

import example.rest.domain.Message;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message,Long> {
    //use query hints
    @EntityGraph(attributePaths = {"comments"})
    List<Message> findAll();
}
