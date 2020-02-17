package example.rest.controller;

import com.fasterxml.jackson.annotation.JsonView;
import example.rest.domain.Message;
import example.rest.domain.User;
import example.rest.domain.Views;
import example.rest.dto.EventType;
import example.rest.dto.MetaDto;
import example.rest.dto.ObjectType;
import example.rest.repo.MessageRepo;
import example.rest.utils.WebSocketSender;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.addAll;


@RestController
@RequestMapping("message")
public class MessageController {

    private static String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";
    private static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";

    private static Pattern URL_REGEX = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);
    private static Pattern IMG_REGEX = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);

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


    /**
     *
     * @param message
     * @param user - author comment
     * @return
     */
    @PostMapping
    public Message create(@RequestBody Message message, @AuthenticationPrincipal User user) throws IOException {
        message.setLocalDateTime(LocalDateTime.now());
        fillMeta(message);
        message.setAuthor(user);
        Message updatedMessages = messageRepo.save(message);
        webSocketSender.accept(EventType.CREATE, updatedMessages);
        return updatedMessages;

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
        BeanUtils.copyProperties(message, messageFromDb, "id");
        fillMeta(message);
        Message updatedMessages = messageRepo.save(message);
        webSocketSender.accept(EventType.UPDATE, updatedMessages);

        return updatedMessages;
    }


    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {
        messageRepo.delete(message);
        webSocketSender.accept(EventType.DELETE, message);
    }

    /**
     * for use openGraff protocol and twitter card
     * @param message
     * @throws IOException
     */
    private void fillMeta(Message message) throws IOException {
        String text = message.getText();
        Matcher matcher = URL_REGEX.matcher(text);

        if(matcher.find()){
            String url = text.substring(matcher.start(),matcher.end());
            message.setLink(url);

            matcher = IMG_REGEX.matcher(url);

            if (matcher.find()){
                message.setLinkCover(url);
            }else if (!url.contains("youtu")){

                MetaDto metaDto =  getMeta(url);

                message.setLinkCover(metaDto.getCover());
                message.setLinkTitle(metaDto.getCover());
                message.setLinkDescription(metaDto.getDescription());
            }

        }

    }
    private MetaDto getMeta(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements title = doc.select("meta[name$=title],meta[property$=title]");
        Elements description = doc.select("meta[name$=description],meta[property$=description]");
        Elements cover = doc.select("meta[name$=image],meta[property$=image]");
        return new MetaDto(
                getContent(title.first()),
                getContent(description.first()),
                getContent(cover.first())
        );
    }

    private String getContent(Element element) {
        return element == null ? "" : element.attr("content");
    }


}
