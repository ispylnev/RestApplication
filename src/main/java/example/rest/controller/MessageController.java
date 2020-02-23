package example.rest.controller;

import com.fasterxml.jackson.annotation.JsonView;
import example.rest.domain.Message;
import example.rest.domain.User;
import example.rest.domain.Views;
import example.rest.repo.MessageRepo;
import example.rest.service.MessageService;
import example.rest.utils.WebSocketSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("message")
public class MessageController {

    private final MessageRepo messageRepo;

    private final MessageService messageService;


    @Autowired
    public MessageController(MessageRepo messageRepo, WebSocketSender sender, MessageService messageService) {
        this.messageRepo = messageRepo;
        this.messageService = messageService;

    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Message> list() {
        return messageRepo.findAll();
    }

    @GetMapping("{id}")
    public Message getOne(@PathVariable("id") Message message) {
        return message;
    }


    /**
     * @param message
     * @param user    - author comment
     * @return
     */
    @PostMapping
    public Message create(@RequestBody Message message, @AuthenticationPrincipal User user) throws IOException {

        return messageService.create(message, user);
    }

    //with http
//    @PutMapping("{id}")
//    public Message update(@PathVariable("id") Message messageFromDb, @RequestBody Message message) {
//        BeanUtils.copyProperties(message, messageFromDb, "id");
//        return messageRepo.save(messageFromDb);
//    }

    //with http
    @PutMapping("{id}")
    public Message update(@PathVariable("id") Message messageFromDb, @RequestBody Message message) throws IOException {
        return messageService.update(messageFromDb, message);
    }


    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {
        messageService.delete(message);
    }

}
