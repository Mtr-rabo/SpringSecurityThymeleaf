package org.mtr.sec.web;



import java.security.Principal;

import org.mtr.sec.dto.UserDTO;
import org.mtr.sec.entities.AppUser;
import org.mtr.sec.repo.ProfileRepository;
import org.mtr.sec.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
@AllArgsConstructor
public class UserController {

    private final ProfileRepository profileRepository;
	private final UserService userService;


	
	@GetMapping("/users")
	public String getUsers(
			Model model,
			@RequestParam(name="page",defaultValue= "0") int page,
			@RequestParam(name="size",defaultValue= "5") int size,
			@RequestParam(name="searchTerm",defaultValue= "") String searchTerm) {
		Page<AppUser> listUsers= userService.getAllUsers(searchTerm,page, size);
		model.addAttribute("listeUsers",listUsers.getContent());
		model.addAttribute("pages",new int[listUsers.getTotalPages()]);
		model.addAttribute("currentPage",page);
		model.addAttribute("searchTerm",searchTerm);
		return "users";
	}
	@GetMapping("/deleteUser")
	public String deleteUser(Long id,int currentPage,String searchTerm ) {
		userService.deleteUser(id);
		return "redirect:users?page="+currentPage+"&searchTerm="+searchTerm;
		
	}
	

	@GetMapping("/editUser")
public String editProfile(Model model,
                          @RequestParam(required = false) Long id,
                          @RequestParam(defaultValue = "0") int currentPage,
                          @RequestParam(defaultValue = "") String searchTerm) {
    if (id != null) {
    			AppUser appUser = userService.getUserById(id);
    			UserDTO userDTO = UserDTO.builder()
						.id(appUser.getId())
						.username(appUser.getUsername())
						.password(appUser.getPassword())
						.email(appUser.getEmail())
						.profileId(appUser.getProfile().getId())
						.build();
        model.addAttribute("user",userDTO  );
       
        
    } else {
        model.addAttribute("user", new UserDTO());
    }
    model.addAttribute("Profiles", profileRepository.findAll());
    model.addAttribute("searchTerm", searchTerm);
    model.addAttribute("currentPage", currentPage);
    return "updateUser";
}
@GetMapping("/saveUser")
public String saveProfile(@ModelAttribute UserDTO userDto) {
	System.out.println("Saving user: " + userDto);
    userService.createUser(userDto);
    return "redirect:/users"; // Redirect to the profiles list page after saving
}

	
	
	@GetMapping("/showUser")
	public String showUser(Model model, @RequestParam Long id, @RequestParam(defaultValue = "0") int currentPage,
            @RequestParam(defaultValue = "") String searchTerm) {
AppUser appUser = userService.getUserById(id);
	model.addAttribute("user", appUser);
	model.addAttribute("currentPage", currentPage);
	model.addAttribute("searchTerm", searchTerm);
	return "showUser";
	}
	
 @GetMapping("/resetPassword")
 public String resetPassword(Model model, @RequestParam Long id, @RequestParam(defaultValue = "0") int currentPage,
			@RequestParam(defaultValue = "") String searchTerm) {
		AppUser appUser = userService.getUserById(id);
		
		
		String nouveauPassword = userService.generateNouveauSecure(appUser);
		model.addAttribute("user", appUser);
		model.addAttribute("nouveauPassword", nouveauPassword);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("searchTerm", searchTerm);
		return "resetPassword";
 }
	
 
 @GetMapping("/changementMdp")
 public String changementMdp(Model model, @RequestParam String username, @RequestParam(defaultValue = "0") int currentPage,
			@RequestParam(defaultValue = "") String searchTerm) {
		AppUser appUser = userService.getUserByUsername(username);
		model.addAttribute("user", appUser);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("searchTerm", searchTerm);
		return "changementMdp";
	}
 
 

 @PostMapping("/change-password")
 public String updatePassword(
     @RequestParam String currentPassword,
     @RequestParam String newPassword,
     @RequestParam String confirmeNewPassword,
     Principal principal,
     RedirectAttributes redirectAttributes,
     HttpServletRequest request) {

     String username = principal.getName(); // Récupère le username de l'utilisateur connecté

     try {
         if (!newPassword.equals(confirmeNewPassword)) {
             redirectAttributes.addFlashAttribute("error", "Les nouveaux mots de passe ne correspondent pas");
             redirectAttributes.addAttribute("username", username);
             return "redirect:/changementMdp";
         }

         Boolean isChange = userService.changePassword(username, currentPassword, newPassword, confirmeNewPassword);
         
         if (isChange) {
             new SecurityContextLogoutHandler().logout(request, null, null);
             redirectAttributes.addFlashAttribute("successMessage", "Mot de passe changé avec succès. Veuillez vous reconnecter.");
             return "redirect:/login?passwordChanged";
         }

         redirectAttributes.addFlashAttribute("message", "Échec du changement de mot de passe. Veuillez vérifier votre mot de passe actuel.");
         redirectAttributes.addAttribute("username", username);
         return "redirect:/changementMdp";
         
     } catch (Exception e) {
         redirectAttributes.addFlashAttribute("error", e.getMessage());
         redirectAttributes.addAttribute("username", username);
         return "redirect:/changementMdp";
     }
 }

 @GetMapping("/custom-logout")
 public String customLogout(HttpServletRequest request, HttpServletResponse response) {
     // Déconnexion manuelle
     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
     if (auth != null) {
         new SecurityContextLogoutHandler().logout(request, response, auth);
     }
     
     // Redirection vers login avec le message
     return "redirect:/login?passwordChanged";
 }
 
 
 
}
