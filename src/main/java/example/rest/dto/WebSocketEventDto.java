package example.rest.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//Такие классы нужно отправлять по вебсокету. Если нужно расширять. Начинай с EventType->ObjectType-> и создавай класс для отправки
public class WebSocketEventDto {
    private ObjectType objectType;
    private EventType eventType;
    @JsonValue
    private String body;
}
