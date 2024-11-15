package com.employees.demo.dao.repositories;

import com.employees.demo.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByDescription(String description);

    List<Role> findByDescriptionIn(String[] descriptions);

}
