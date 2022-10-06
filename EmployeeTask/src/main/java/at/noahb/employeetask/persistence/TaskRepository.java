package at.noahb.employeetask.persistence;

import at.noahb.employeetask.domain.Employee;
import at.noahb.employeetask.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByAssignedEmployee(Employee employee);

}
