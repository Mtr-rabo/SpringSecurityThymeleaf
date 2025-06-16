package org.mtr.sec.repo;

import java.util.Optional;

import org.mtr.sec.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}