package com.example.e_commerce.service;


import lombok.RequiredArgsConstructor;

import com.example.e_commerce.dto.UserCreateDTO;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
 import com.example.e_commerce.mapper.UserMapper;
import com.example.e_commerce.dto.UserDTO;
import com.example.e_commerce.entity.User;
import com.example.e_commerce.exception.ResourceNotFoundException;
import com.example.e_commerce.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

  @Override
public UserDTO createUser(UserCreateDTO createDTO) {
    User user = userMapper.toEntity(createDTO); // mapper gère le mot de passe
    
    
    if (user.getMotDePasse() != null && !user.getMotDePasse().isBlank()) {
            user.setMotDePasse(passwordEncoder.encode(user.getMotDePasse()));
        }

        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
}

    @Override
    public UserDTO updateUser(Long id, UserDTO userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable avec id: " + id));

        user.setNom(userDto.getNom());
        user.setEmail(userDto.getEmail());
        user.setPays(userDto.getPays());
        user.setTelephone(userDto.getTelephone());
        user.setRegion(userDto.getRegion());
        user.setDepartement(userDto.getDepartement());
        user.setRole(userDto.getRole());

        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable avec id: " + id));
        userRepository.delete(user);
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable avec id: " + id));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }
}

