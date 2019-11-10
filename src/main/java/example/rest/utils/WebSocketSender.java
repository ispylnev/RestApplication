package example.rest.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import example.rest.dto.EventType;
import example.rest.dto.ObjectType;
import example.rest.dto.WebSocketEventDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
public class WebSocketSender {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ObjectMapper mapper;

    public WebSocketSender(SimpMessagingTemplate simpMessagingTemplate, ObjectMapper mapper) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.mapper = mapper;
    }

    /**
     * @param objectType- object sent to clietn
     * @param jsonView    - view json
     * @param <T>         - model for send to websocket
     * @return
     */
    public <T> BiConsumer<EventType, T> getSender(ObjectType objectType, Class jsonView) {
        ObjectWriter writer = getObjectWithConfigMapper(jsonView);
        return (EventType eventType, T payload)
                -> {
            String value = null;
            try {
                value = writer.writeValueAsString(payload);
                simpMessagingTemplate.convertAndSend("/topic/activity",
                        new WebSocketEventDto(objectType, eventType, value));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        };
    }

    //чтобы не мапить  все поля
    private ObjectWriter getObjectWithConfigMapper(Class view) {
        return mapper
                .setConfig(mapper.getSerializationConfig())
                .writerWithView(view);

    }
}
