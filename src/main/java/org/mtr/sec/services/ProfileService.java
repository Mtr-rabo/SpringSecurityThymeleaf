package org.mtr.sec.services;


import java.util.List;

import org.mtr.sec.dto.ProfileDTO;
import org.mtr.sec.entities.AppUser;
import org.mtr.sec.entities.Profile;
import org.mtr.sec.repo.ProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
	 private final ProfileRepository profileRepository;
	 
	public Profile createProfile(ProfileDTO dto) {
		Profile profile=Profile.builder().id(dto.getId()).name(dto.getName()).roles(dto.getRoles()).build();
        return profileRepository.save(profile);
    }



	    public List<Profile> getAllProfile() {
	        return profileRepository.findAll();
	    }

	    public Profile updateProfile(Long id, ProfileDTO dto) {
	    	Profile profile = profileRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("profile not found"));

	        profile.setName(dto.getName());
	        profile.setRoles(dto.getRoles());
	        

	        return profileRepository.save(profile);
	    }
	    public Profile getProfileById(Long id) {
	       

	        return profileRepository.findById(id)
		            .orElseThrow(() -> new RuntimeException("profile not found"));
	    }
	    public Page<Profile> getAllProfileRechercher(String searchTerm,int page, int size) {
	        return profileRepository.findByNameContains(searchTerm,PageRequest.of(page, size));
	    }
	    public void deleteProfile(Long id) {
	        profileRepository.deleteById(id);
	    }
}
