package example.rest.controller;

import com.fasterxml.jackson.annotation.JsonView;
import example.rest.domain.Message;
import example.rest.domain.Views;
import example.rest.dto.EventType;
import example.rest.dto.ObjectType;
import example.rest.repo.MessageRepo;
import example.rest.utils.WebSocketSender;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;


@RestController
@RequestMapping("message")
public class MessageController {

    private final MessageRepo messageRepo;
    private final BiConsumer<EventType, Message> webSocketSender;

    @Autowired
    public MessageController(MessageRepo messageRepo, WebSocketSender sender) {
        this.messageRepo = messageRepo;
        this.webSocketSender = sender.getSender(ObjectType.MESSAGE, Views.IdName.class);
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
        Message updatedMessages = messageRepo.save(message);
        webSocketSender.accept(EventType.CREATE, updatedMessages);
        return updatedMessages;

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

//    //Отвечает за мэппинг для запросов по webSocket
//    @MessageMapping("/changeMessage")
//    //topic for subscribe the client
//    @SendTo("/topic/activity")
//    public Message message(Message message) {
//        String test;
//        return messageRepo.save(message);
//    }
}
