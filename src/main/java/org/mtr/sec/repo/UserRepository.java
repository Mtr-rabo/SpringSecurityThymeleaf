package org.mtr.sec.repo;

import java.util.Optional;

import org.mtr.sec.entities.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    Page<AppUser>  findByUsernameContains(String searchTerm,Pageable pageable);
}