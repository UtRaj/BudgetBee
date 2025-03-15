package com.backend.service.Impl;

import com.backend.dto.UserDTO;
import com.backend.exception.AlreadyExistsException;
import com.backend.exception.NotFoundException;
import com.backend.model.User;
import com.backend.repository.UserRepository;
import com.backend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {
    private UserDTO userDTO;
    private User user;
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;

    @Captor
    ArgumentCaptor<User> userArgumentCaptor;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository);
        userDTO = new UserDTO("fName", "lName", "test@gmail.com", "password");
        user = new User(userDTO);
    }

    @Test
    public void testAddUser() {
        Mockito.doReturn(user).when(userRepository).save(userArgumentCaptor.capture());
        Mockito.doReturn(null).when(userRepository).findByEmail(userDTO.getEmail());

        userService.addUser(userDTO);

        Mockito.verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
        User returnedUser = userArgumentCaptor.getValue();
        Mockito.verify(userRepository, times(1)).save(returnedUser);
        assertEquals(returnedUser.getFirstName(), userDTO.getFirstName());
        assertEquals(returnedUser.getLastName(), userDTO.getLastName());
        assertEquals(returnedUser.getEmail(), userDTO.getEmail());
        assertEquals(returnedUser.getPassword(), userDTO.getPassword());
    }

    @Test
    public void testAddUserAlreadyExists() {
        Mockito.doReturn(user).when(userRepository).findByEmail(userDTO.getEmail());

        Assertions.assertThrows(AlreadyExistsException.class, () ->
                userService.addUser(userDTO),
                "User already exists with email id: " + userDTO.getEmail());
    }

    @Test
    public void testGetUser() {
        Mockito.doReturn(user).when(userRepository).findByEmail(userDTO.getEmail());

        UserDTO returnedUserDTO = userService.getUser(userDTO.getEmail());

        assertEquals(returnedUserDTO.getFirstName(), user.getFirstName());
        assertEquals(returnedUserDTO.getLastName(), user.getLastName());
        assertEquals(returnedUserDTO.getEmail(), user.getEmail());
        assertEquals(returnedUserDTO.getPassword(), null);
    }

    @Test
    public void testGetUserNotFound() {
        Mockito.doReturn(null).when(userRepository).findByEmail(userDTO.getEmail());

        Assertions.assertThrows(NotFoundException.class, () ->
                userService.getUser(user.getEmail()),
                "No user found");
    }
}
