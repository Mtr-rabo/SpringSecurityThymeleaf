package org.mtr.sec;

import org.mtr.sec.entities.AppUser;
import org.mtr.sec.entities.Profile;
import org.mtr.sec.entities.Role;
import org.mtr.sec.repo.ProfileRepository;
import org.mtr.sec.repo.RoleRepository;
import org.mtr.sec.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class GestionUtilisateursThymeleafApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionUtilisateursThymeleafApplication.class, args);
	}

	
    @Bean
    CommandLineRunner commandLineRunner(
            RoleRepository roleRepository,
            ProfileRepository profileRepository,
            UserRepository userRepository
            ,
          PasswordEncoder passwordEncoder
            ) {
        return args -> {

            // Création du rôle ROLE_ADMIN s'il n'existe pas déjà
        	Role adminRole = roleRepository.findByName("ROLE_ADMIN")
        			 .orElseGet(() -> {
                         Role role = new Role();
                         role.setName("ROLE_ADMIN");
                     
                         return roleRepository.save(role);
                     });

        	  // Création du rôle ROLE_USER s'il n'existe pas déjà
        	Role userRole = roleRepository.findByName("ROLE_USER")
        			 .orElseGet(() -> {
                         Role role = new Role();
                         role.setName("ROLE_USER");
                     
                         return roleRepository.save(role);
                     });

            // Création du profil ADMIN_PROFILE s'il n'existe pas déjà
            Profile adminProfile = profileRepository.findByName("ADMIN_PROFILE")
                    .orElseGet(() -> {
                        Profile profile = new Profile();
                        profile.setName("ADMIN_PROFILE");
                        profile.getRoles().add(adminRole);
                        profile.getRoles().add(userRole);
                        return profileRepository.save(profile);
                    });
            // Création du profil USER_PROFILE s'il n'existe pas déjà
            Profile userProfile = profileRepository.findByName("USER_PROFILE")
                    .orElseGet(() -> {
                        Profile profile = new Profile();
                        profile.setName("USER_PROFILE");
                        profile.getRoles().add(userRole);
                        return profileRepository.save(profile);
                    });
            // Création de l'utilisateur admin s'il n'existe pas déjà
            if (userRepository.findByUsername("admin").isEmpty()) {
                AppUser adminUser = new AppUser();
                adminUser.setUsername("admin");
                adminUser.setEmail("admin@example.com");
                adminUser.setPassword(
                		passwordEncoder.encode("123456")
                		);
                adminUser.setProfile(adminProfile);

                userRepository.save(adminUser);
                System.out.println("✅ Utilisateur admin créé !");
            } else {
                System.out.println("ℹ️ L'utilisateur admin existe déjà.");
            }
            
            // Création de l'utilisateur user1 s'il n'existe pas déjà
            if (userRepository.findByUsername("user1").isEmpty()) {
                AppUser user1 = new AppUser();
                user1.setUsername("user1");
                user1.setEmail("user1@example.com");
                user1.setPassword(
                		passwordEncoder.encode("123456")
                		);
                user1.setProfile(userProfile);

                userRepository.save(user1);
                System.out.println("✅ Utilisateur user1 créé !");
            } else {
                System.out.println("ℹ️ L'utilisateur user1 existe déjà.");
            }
            // Création des utilisateurs user0 à user5
	        for (int i = 0; i <= 30; i++) {
	            String username = "user" + i;
	            String email = username + "@example.com";

	            if (userRepository.findByUsername(username).isEmpty()) {
	                AppUser user = new AppUser();
	                user.setUsername(username);
	                user.setEmail(email);
	                user.setPassword(passwordEncoder.encode("12345")); // Même mot de passe pour tous
	                user.setProfile(userProfile);

	                userRepository.save(user);
	                System.out.println("✅ Utilisateur " + username + " créé !");
	            } else {
	                System.out.println("ℹ️ L'utilisateur " + username + " existe déjà.");
	            }
	        }
        };
        }

}
