package at.noahb.springdonations.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Entity
@Table(name = "donations")

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Min(5)
    private int amount;

    @PastOrPresent
    private LocalDate depositDate;

    @ManyToOne
    @JoinColumn(name = "person")
    private Person person;

    public Donation(int amount, LocalDate depositDate, Person person) {
        this.amount = amount;
        this.depositDate = depositDate;
        this.person = person;
    }

}
