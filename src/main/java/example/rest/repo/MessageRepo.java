package example.rest.repo;

import example.rest.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message,Long> {
    //use query hints
    @EntityGraph(attributePaths = {"comments"})
    Page<Message> findAll(Pageable pageable);
}
