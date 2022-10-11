package at.noahb.personinterest.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Setter
    @OneToMany
    private Set<Interest> interests = new HashSet<>();
    @Setter
    private String firstName;
    @Setter
    private String lastName;
    @Setter
    @Enumerated
    private Sex sex;

    private LocalDate dateOfBirth;
}
