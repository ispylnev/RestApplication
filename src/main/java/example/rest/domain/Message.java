package example.rest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Data
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "text"})
public class Message {

    @Id
    @GeneratedValue
    @JsonView(Views.IdName.class)
    private Long id;
    @JsonView(Views.IdName.class)
    private String text;

    @ManyToOne
    @JsonView(Views.FullMessage.class)
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(mappedBy = "message", orphanRemoval = true)
    @JsonView(Views.FullMessage.class)
    private List<Comment> commentsList;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonView(Views.FullMessage.class)
    private LocalDateTime localDateTime;

    //for micro-marking twitter card and open graph protocol
    @JsonView({Views.FullMessage.class})
    private String link;
    @JsonView({Views.FullMessage.class})
    private String linkTitle;
    @JsonView({Views.FullMessage.class})
    private String linkDescription;
    @JsonView({Views.FullMessage.class})
    private String linkCover;


}
