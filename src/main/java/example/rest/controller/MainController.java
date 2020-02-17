package example.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import example.rest.domain.Message;
import example.rest.domain.User;
import example.rest.domain.Views;
import example.rest.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;


//for HTML page 'index'
@Controller
@RequestMapping("/")
public class MainController {

    final MessageRepo messageRepo;

    @Value("${spring.profiles.active}")
    private String profile;

    //для удаления не нужных полей
    private final ObjectWriter writer;

    /**
     *
     * @param messageRepo
     * @param objectMapper - for delete unnecessary fields
     */
    @Autowired
    public MainController(MessageRepo messageRepo, ObjectMapper objectMapper) {
        this.messageRepo = messageRepo;
        this.writer = objectMapper
                .setConfig(objectMapper.getSerializationConfig())
                .writerWithView(Views.FullMessage.class);
    }


    @GetMapping
    public String main(Model model, @AuthenticationPrincipal User user) throws JsonProcessingException {

        HashMap<Object, Object> data = new HashMap<>();
        if (user != null) {
            data.put("profile", user);
            String messages =  writer.writeValueAsString(messageRepo.findAll());
            model.addAttribute("messages", messages);
        }

        model.addAttribute("frontendData", data);
        model.addAttribute("isDevMode", "dev".equals(profile));
        return "index";
    }
}
