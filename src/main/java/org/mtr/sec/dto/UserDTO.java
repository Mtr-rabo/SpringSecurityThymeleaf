package org.mtr.sec.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class UserDTO {
		private Long id;
    private String username;
    private String password;
    private String email;
    private Long profileId;
}
