package org.mtr.sec.services;

import java.util.List;

import org.mtr.sec.dto.RoleDTO;

import org.mtr.sec.entities.Role;

import org.mtr.sec.repo.RoleRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
  

    public Role createRole(RoleDTO dto) {
      

       Role role = Role.builder().name(dto.getName())
            .build();

        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role updateRole(Long id, RoleDTO dto) {
     Role role = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Role not found"));

        role.setName(dto.getName());
        

        return roleRepository.save(role);
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
