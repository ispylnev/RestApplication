package example.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import example.rest.domain.User;
import example.rest.domain.Views;
import example.rest.dto.MessagePageDto;
import example.rest.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;


//for HTML page 'index'
@Controller
@RequestMapping("/")
public class MainController {

    private final MessageService messageService;

    @Value("${spring.profiles.active}")
    private String profile;

    //для удаления не нужных полей
    private final ObjectWriter writer;

    /**
     * @param messageService
     * @param objectMapper   - for delete unnecessary fields
     */
    @Autowired
    public MainController(MessageService messageService, ObjectMapper objectMapper) {
        this.messageService = messageService;
        this.writer = objectMapper
                .setConfig(objectMapper.getSerializationConfig())
                .writerWithView(Views.FullMessage.class);
    }


    @GetMapping
    public String main(Model model, @AuthenticationPrincipal User user) throws JsonProcessingException {

        HashMap<Object, Object> data = new HashMap<>();
        if (user != null) {

            PageRequest start = startDefaultMessageList();
            MessagePageDto messagePageDto = messageService.findAll(start);

            data.put("profile", user);
            String messages = writer.writeValueAsString(messagePageDto);

            model.addAttribute("messages", messages);
            data.put("currentPage",messagePageDto.getCurrentPage());
            data.put("totalPages",messagePageDto.getTotalPages());
        } else {
            model.addAttribute("messages", "[]");
        }

        model.addAttribute("frontendData", data);
        model.addAttribute("isDevMode", "dev".equals(profile));
        return "index";
    }

    private PageRequest startDefaultMessageList() {

        Sort sortById = Sort.by(Sort.Direction.DESC, "id");
        return PageRequest.of(0, MessageController.MESSAGES_DEF_PER_PAGE, sortById);
    }
}
