package at.noahb.employeetask.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter

@Entity
@Table(name = "Tasks")
public class Task {

    @Id
    @GeneratedValue
    Integer id;

    @Setter
    @NotNull
    String description;

    @Setter
    @Past
    @Column(name = "finished_date")
    LocalDate finishingTime;

    @Positive
    Integer hoursWorked;

    @ManyToOne
    @Setter
    @JoinColumn(name = "employee_id")
    Employee assignedEmployee;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Task task = (Task) o;
        return id != null && Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
