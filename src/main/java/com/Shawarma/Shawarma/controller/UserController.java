package com.Shawarma.Shawarma.controller;

import com.Shawarma.Shawarma.model.AppUser;
import com.Shawarma.Shawarma.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public AppUser registerUser(@RequestBody AppUser user) {
        return userService.registerUser(user);
    }

    @GetMapping("/byUsername/{username}")
    public AppUser getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
