package example.rest.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "usr")
public class User {
    @Id
    private String id;
    private String name;
    private String userPic;
    private String email;
    private String gender;
    private String locale;
    private LocalDate localVisit;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
