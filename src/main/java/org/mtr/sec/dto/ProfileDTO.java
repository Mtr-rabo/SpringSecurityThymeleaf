package org.mtr.sec.dto;

import java.util.HashSet;
import java.util.Set;

import org.mtr.sec.entities.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class ProfileDTO {
		private Long id;
	    private String name;
	    private Set<Role> roles = new HashSet<>();

}
