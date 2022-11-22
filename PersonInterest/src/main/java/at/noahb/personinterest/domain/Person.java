package at.noahb.personinterest.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Integer personId;

    //@ManyToMany(fetch = FetchType.EAGER)
    @ManyToMany
    @Size(max = 3)
    private Set<Interest> interests = new HashSet<>();

    @Pattern(regexp = "^[A-Z].*", message = "capital letter")
    @Size(max = 30)
    @NotBlank(message = "Please enter a first name")
    private String firstName;

    @Pattern(regexp = "^[A-Z].*", message = "capital letter")
    @Size(max = 30)
    @NotBlank(message = "Please enter a last name")
    private String lastName;
    @Enumerated
    private Sex sex;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Date of birth must be in the past or today")
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
                "id=" + personId +
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
        return Objects.equals(personId, person.personId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId);
    }
}
