package example.rest.dto;

import com.fasterxml.jackson.annotation.JsonView;
import example.rest.domain.Message;
import example.rest.domain.Views;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonView(Views.FullMessage.class)
public class MessagePageDto {

    private List<Message> messageList;
    private int currentPage;
    private int totalPages;

}
