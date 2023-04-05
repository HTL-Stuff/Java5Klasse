package at.noahb.userverwaltung.domain.persistent;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "questions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(max = 20, message = "Name must be less than 20 characters long")
    @NotNull(message = "Name must not be null")
    @Setter
    private String name;

    @Length(max = 200, message = "Question must be less than 200 characters long")
    @NotNull(message = "Question must not be null")
    @Setter
    private String question;

    @FutureOrPresent(message = "Expiry date must be in the future or present")
    @Setter
    private LocalDate expiryDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "question_answers",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "answer_id"))
    private Set<Answer> answers;

    public Question(String name, String question, LocalDate expiryDate) {
        this.name = name;
        this.question = question;
        this.expiryDate = expiryDate;
    }

    public Question(String name, String question, LocalDate expiryDate, Set<Answer> answers) {
        this.name = name;
        this.question = question;
        this.expiryDate = expiryDate;
        this.answers = answers;
    }

    public void addAnswer(Answer answer) {
        if (answers.stream().map(Answer::getAnswerer).anyMatch(answer.getAnswerer()::equals)) {
            return;
        }
        answers.add(answer);
    }

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
