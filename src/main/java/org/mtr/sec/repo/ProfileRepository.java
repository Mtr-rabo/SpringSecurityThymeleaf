package org.mtr.sec.repo;

import java.util.Optional;

import org.mtr.sec.entities.AppUser;
import org.mtr.sec.entities.Profile;
import org.mtr.sec.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProfileRepository extends JpaRepository<Profile, Long> {
	
	Optional<Profile> findByName(String name);
	 boolean existsByName(String name);

	Page<Profile> findByNameContains(String searchTerm, Pageable pageable);
	
}