package example.rest.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jboss.logging.LogMessage;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @JsonProperty("id")
    private Long id;
    @JsonView(Views.IdName.class)
    private String text;

    @ManyToOne
    @JsonView(Views.FullMessage.class)
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(mappedBy = "message",
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JsonView(Views.IdName.class)
    private List<Comment> comments = new ArrayList<>();

    public void setComments(List<Comment> comments) {
        if (comments == null) {
            this.comments = new ArrayList<>();
            return;
        }
        this.comments.clear();
        this.comments.addAll(comments);
    }


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
