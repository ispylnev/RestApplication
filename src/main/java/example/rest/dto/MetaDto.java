package example.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MetaDto {

    private String title;
    private String description;
    private String cover;
}
