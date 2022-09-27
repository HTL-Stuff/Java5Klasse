package at.noahb.springdonations.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Donation donation = (Donation) o;
        return id != null && Objects.equals(id, donation.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
