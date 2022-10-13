package at.noahb.personinterest.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_id")
    private Integer interestId;

    @Setter
    @Column(unique = true)
    private String description;

    public Interest(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interest interest = (Interest) o;
        return Objects.equals(interestId, interest.interestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(interestId);
    }


}
