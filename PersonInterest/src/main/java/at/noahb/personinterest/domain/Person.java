package at.noahb.personinterest.domain;

import lombok.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Interest> interests = new HashSet<>();
    @Setter
    private String firstName;
    @Setter
    private String lastName;
    @Setter
    @Enumerated
    private Sex sex;

    private LocalDate dateOfBirth;

    public Person(Set<Interest> interests, String firstName, String lastName, Sex sex, LocalDate dateOfBirth) {
        this.interests = interests;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
    }

    public String getInterestsPretty() {
        return interests.stream().map(Interest::getDescription).collect(Collectors.joining(","));
    }


    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", interests=" + interests +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", sex=" + sex +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
