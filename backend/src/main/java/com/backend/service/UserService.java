package com.backend.service;

import com.backend.dto.UserDTO;

public interface UserService {
    void addUser(UserDTO userDTO);

    UserDTO getUser(String email);
}
