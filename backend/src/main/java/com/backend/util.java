package com.backend;

import com.backend.dto.UserDTO;
import org.springframework.security.core.context.SecurityContextHolder;

public class util {
    public static String getUserEmail() {
        return ((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
    }
}
