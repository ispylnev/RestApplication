package example.rest.controller;

import com.fasterxml.jackson.annotation.JsonView;
import example.rest.domain.Message;
import example.rest.domain.Views;
import example.rest.repo.MessageRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("message")
public class MessageController {

    private final MessageRepo messageRepo;

    @Autowired
    public MessageController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
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


    @PostMapping
    public Message create(@RequestBody Message message) {
        message.setLocalDateTime(LocalDateTime.now());
        return messageRepo.save(message);

    }

    @PutMapping("{id}")
    public Message update(@PathVariable("id") Message messageFromDb, @RequestBody Message message) {
        BeanUtils.copyProperties(message, messageFromDb, "id");
        return messageRepo.save(messageFromDb);
    }


    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {
        messageRepo.delete(message);
    }

    //Отвечает за мэппинг для запросов по webSocket
    @MessageMapping("/changeMessage")
    //topic for subscribe the client
    @SendTo("/topic/activity")
    public Message message(Message message) {
        String test;
        return messageRepo.save(message);

    }
}
