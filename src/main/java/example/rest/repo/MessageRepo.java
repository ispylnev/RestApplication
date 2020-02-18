package example.rest.repo;

import example.rest.domain.Message;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message,Long> {
    //use query hints
    @EntityGraph(attributePaths = {"comments"})
    List<Message> findAll();
}
