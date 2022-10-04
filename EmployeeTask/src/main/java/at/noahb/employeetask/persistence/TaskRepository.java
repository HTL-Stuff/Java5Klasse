package at.noahb.employeetask.persistence;

import at.noahb.employeetask.domain.Employee;
import at.noahb.employeetask.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select sum(t.hoursWorked) from Task t where t.assignedEmployee = ?1")
    long getTotalHoursWorkedByEmployee(Employee employee);


    @Query("select t from Task t where t.assignedEmployee = ?1 and (t.finishingTime between ?2 and ?3)")
    List<Task> getTaskByAssignedEmployeeBetween(Employee employee, LocalDate from, LocalDate to);

    List<Task> findAllByAssignedEmployee(Employee employee);

}
