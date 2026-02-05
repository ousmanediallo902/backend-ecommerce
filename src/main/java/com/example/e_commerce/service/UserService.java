package com.example.e_commerce.service;

import com.example.e_commerce.dto.UserCreateDTO;
import com.example.e_commerce.dto.UserDTO;
import java.util.List;

public interface UserService {
    UserDTO createUser(UserCreateDTO createDTO);
    UserDTO updateUser(Long id, UserDTO userDto);
    void deleteUser(Long id);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
}

