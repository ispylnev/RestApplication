package example.rest.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class WebSocketEventDto {

    private ObjectType objectType;
    private EventType eventType;
    @JsonValue
    private String body;
}
