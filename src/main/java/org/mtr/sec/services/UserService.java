package org.mtr.sec.services;

import java.security.SecureRandom;
import java.util.List;

import org.mtr.sec.dto.UserDTO;
import org.mtr.sec.entities.AppUser;
import org.mtr.sec.entities.Profile;
import org.mtr.sec.repo.ProfileRepository;
import org.mtr.sec.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class UserService {

	
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();
    
    
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
   private final PasswordEncoder passwordEncoder;

    public AppUser createUser(UserDTO dto) {
        Profile profile = profileRepository.findById(dto.getProfileId())
            .orElseThrow(() -> new RuntimeException("Profile not found"));

        AppUser user = AppUser.builder().id(dto.getId())
            .username(dto.getUsername())
            .password(
            		passwordEncoder.encode(dto.getPassword())
            		
            		)
            .email(dto.getEmail())
            .enabled(true)
            .profile(profile)
            .build();
        return userRepository.save(user);
    }

    public Page<AppUser> getAllUsers(String searchTerm,int page, int size) {
        return userRepository.findByUsernameContains(searchTerm,PageRequest.of(page, size));
    }

    public AppUser updateUser(Long id, UserDTO dto) {
        AppUser user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(dto.getUsername());
        user.setPassword(
        		passwordEncoder.encode(dto.getPassword())
        		);
        user.setProfile(profileRepository.findById(dto.getProfileId())
            .orElseThrow(() -> new RuntimeException("Profile not found")));

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

	public AppUser getUserById(Long id) {

		return userRepository.findById(id).get();
	}
	public AppUser getUserByUsername(String name) {

		return userRepository.findByUsername(name).get();
	}
	
	   public static String generateSecurePassword(int length) {
	        StringBuilder sb = new StringBuilder(length);

	        for (int i = 0; i < length; i++) {
	            int rndCharIndex = random.nextInt(ALPHABET.length());
	            sb.append(ALPHABET.charAt(rndCharIndex));
	        }

	        return sb.toString();
	    }

	   public String generateNouveauSecure(AppUser user) {
	       String newPassword = generateSecurePassword(10);
	       user.setPassword(passwordEncoder.encode(newPassword));
	       userRepository.save(user);
	       return newPassword;
	   }

	   public Boolean changePassword(String username,String currentPassword, String newPassword, String confirmeNewPassword) {
	
		AppUser user = userRepository.findByUsername(username).get();
		System.out.println("======================================old password: " + user.getPassword());

		if (passwordEncoder.matches(currentPassword, user.getPassword())) {
			System.out.println("======================================newPassword password: " + newPassword);
			user.setPassword(passwordEncoder.encode(newPassword));
			
		AppUser appUser=	userRepository.save(user);
		System.out.println("=============================Password changed for user: " + appUser.getPassword());
			return true;
		} else {
			 return false;
			
		}
		
	   }
	   
	   
}
