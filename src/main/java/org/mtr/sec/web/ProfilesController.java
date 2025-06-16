package org.mtr.sec.web;

import org.mtr.sec.dto.ProfileDTO;
import org.mtr.sec.entities.AppUser;
import org.mtr.sec.entities.Profile;
import org.mtr.sec.services.ProfileService;
import org.mtr.sec.services.RoleService;
import org.mtr.sec.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AllArgsConstructor;
@Controller
@AllArgsConstructor
public class ProfilesController {
	private final ProfileService profileService;
	private final RoleService roleService;
	
	@GetMapping("/")
	public String home() {
		return "redirect:/users";
	}
	
	@GetMapping("/profiles")
	public String getProfiles(
			Model model,
			@RequestParam(name="page",defaultValue= "0") int page,
			@RequestParam(name="size",defaultValue= "5") int size,
			@RequestParam(name="searchTerm",defaultValue= "") String searchTerm) {
		Page<Profile> listProfiles= profileService.getAllProfileRechercher(searchTerm, page, size);
		model.addAttribute("listeProfiles",listProfiles.getContent());
		model.addAttribute("pages",new int[listProfiles.getTotalPages()]);
		model.addAttribute("currentPage",page);
		model.addAttribute("searchTerm",searchTerm);
		return "profiles";
	}
	
	@GetMapping("/editProfile")
public String editProfile(Model model,
                          @RequestParam(required = false) Long id,
                          @RequestParam(defaultValue = "0") int currentPage,
                          @RequestParam(defaultValue = "") String searchTerm) {
    if (id != null) {
        model.addAttribute("profile", profileService.getProfileById(id));
        System.out.println("Saving profile: " + profileService.getProfileById(id));
        
    } else {
        model.addAttribute("profile", new Profile());
    }
    model.addAttribute("roles", roleService.getAllRoles());
    model.addAttribute("searchTerm", searchTerm);
    model.addAttribute("currentPage", currentPage);
    return "updateProfile";
}
@GetMapping("/saveProfile")
public String saveProfile(@ModelAttribute Profile profile) {
	
	
	ProfileDTO profileDTO = ProfileDTO.builder().id(profile.getId()).name(profile.getName()).roles(profile.getRoles())
			.build();
    profileService.createProfile(profileDTO);
    return "redirect:/profiles"; // Redirect to the profiles list page after saving
}

	
	@GetMapping("/deleteProfile")
	public String deleteProfile(Long id,int currentPage,String searchTerm ) {
		profileService.deleteProfile(id);
		return "redirect:profiles?page="+currentPage+"&searchTerm="+searchTerm;
		
	}
	@GetMapping("/showProfile")
	public String showProfile(Model model, @RequestParam Long id, @RequestParam(defaultValue = "0") int currentPage,
            @RequestParam(defaultValue = "") String searchTerm) {
Profile profile = profileService.getProfileById(id);
	model.addAttribute("profile", profile);
	model.addAttribute("currentPage", currentPage);
	model.addAttribute("searchTerm", searchTerm);
	return "showProfile";}
	

}
