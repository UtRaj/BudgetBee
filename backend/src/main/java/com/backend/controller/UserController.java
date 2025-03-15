package com.backend.controller;

import com.backend.dto.UserDTO;
import com.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    @PostMapping("/signup")
    public ResponseEntity saveUser(@RequestBody UserDTO userDTO){
        userService.addUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("/get")
    public ResponseEntity getUser(@RequestParam("email") String email){
        UserDTO userDTO = userService.getUser(email);

        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }
}
