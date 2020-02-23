package example.rest.controller;

import com.fasterxml.jackson.annotation.JsonView;
import example.rest.domain.Message;
import example.rest.domain.User;
import example.rest.domain.Views;
import example.rest.dto.MessagePageDto;
import example.rest.repo.MessageRepo;
import example.rest.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("message")
public class MessageController {

    private final MessageRepo messageRepo;

    private final MessageService messageService;

    public static  final int MESSAGES_DEF_PER_PAGE = 3;


    @Autowired
    public MessageController(MessageRepo messageRepo, MessageService messageService) {
        this.messageRepo = messageRepo;
        this.messageService = messageService;

    }

    @GetMapping
    @JsonView(Views.FullMessage.class)
    public MessagePageDto pageDto(@PageableDefault(size = MESSAGES_DEF_PER_PAGE, sort = {"id"},
            direction = Sort.Direction.DESC)
                                          Pageable page) {

        return messageService.findAll(page);
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
