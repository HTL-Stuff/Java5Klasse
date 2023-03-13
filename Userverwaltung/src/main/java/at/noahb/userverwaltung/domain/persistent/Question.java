package at.noahb.userverwaltung.domain.persistent;

import at.noahb.userverwaltung.domain.Answer;
import at.noahb.userverwaltung.domain.AnswerType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity

@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(max = 20)
    @NotNull
    private String name;

    @Length(max = 200)
    @NotNull
    private String question;

    @PastOrPresent
    private LocalDate expiryDate;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Answer> answers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
