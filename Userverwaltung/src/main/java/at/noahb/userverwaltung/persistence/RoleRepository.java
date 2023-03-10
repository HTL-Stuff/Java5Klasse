package at.noahb.userverwaltung.persistence;

import at.noahb.userverwaltung.domain.persistent.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
