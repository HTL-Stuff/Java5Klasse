package at.noahb.vmm.domain;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter

@Entity
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gradeId;

    @NotNull
    @PastOrPresent
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Subject subject;

    @Range(min = 1, max = 5)
    @NotNull
    private Integer grade;

    @ToString.Exclude
    @ManyToOne
    private Student student;

    public Grade(LocalDate date, Subject subject, Integer grade, Student student) {
        this.date = date;
        this.subject = subject;
        this.grade = grade;
        this.student = student;
    }
}
