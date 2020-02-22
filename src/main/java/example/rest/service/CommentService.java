package example.rest.service;

import com.fasterxml.jackson.annotation.JsonView;
import example.rest.domain.Comment;
import example.rest.domain.Message;
import example.rest.domain.User;
import example.rest.domain.Views;
import example.rest.dto.EventType;
import example.rest.dto.ObjectType;
import example.rest.repo.CommentRepo;
import example.rest.utils.WebSocketSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

@Service
public class CommentService {

    private final CommentRepo commentRepo;

    private final BiConsumer<EventType, Comment> webSocketSender;

    @Autowired
    public CommentService(CommentRepo commentRepo, WebSocketSender sender) {
        this.commentRepo = commentRepo;
        this.webSocketSender = sender.getSender(ObjectType.COMMENTS, Views.FullComment.class);
    }

    @JsonView(Views.FullComment.class)
    public Comment create(Comment comment, User user) {
        comment.setAuthor(user);
        Comment commentFromDb = commentRepo.save(comment);
        webSocketSender.accept(EventType.CREATE, commentFromDb);

        return comment;
    }


}
