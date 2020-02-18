package example.rest.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;
import example.rest.domain.Views;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonView(Views.Id.class)
//Такие классы нужно отправлять по вебсокету. Если нужно расширять. Начинай с EventType->ObjectType-> и создавай класс для отправки
public class WebSocketEventDto {
    private ObjectType objectType;
    private EventType eventType;
    @JsonRawValue
    private String body;
}
