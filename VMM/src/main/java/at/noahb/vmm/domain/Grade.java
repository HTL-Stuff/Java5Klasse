package at.noahb.vmm.domain;

import lombok.*;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.autoconfigure.web.WebProperties;

import javax.persistence.*;
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

    private LocalDate date;

    private String subject;

    @Range(min = 1, max = 5)
    private Integer grade;

    @ToString.Exclude
    @ManyToOne
    private Student student;

    public Grade(LocalDate date, String subject, Integer grade, Student student) {
        this.date = date;
        this.subject = subject;
        this.grade = grade;
        this.student = student;
    }
}
