package com.task314.controller;

import com.task314.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.task314.service.UserService;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String executeOperations() {
        userService.getAllUsers();

        User newUser = new User(3L, "James", "Brown", (byte) 35);
        String part1 = userService.saveUser(newUser);

        newUser.setName("Thomas");
        newUser.setLastName("Shelby");
        String part2 = userService.updateUser(newUser);

        String part3 = userService.deleteUser(3L);

        return part1 + part2 + part3;
    }
}