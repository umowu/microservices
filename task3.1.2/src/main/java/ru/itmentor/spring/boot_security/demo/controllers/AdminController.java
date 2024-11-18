package ru.itmentor.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.services.UserService;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAdminPanel(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.findAllRoles());
        return "user_form";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute User user, @RequestParam(value = "roleStr", required = false) Set<String> roleStr) {
        user.setRole(userService.toSetRoles(roleStr));
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/admin";
        }
        model.addAttribute("user", user);
        model.addAttribute("roles", userService.findAllRoles());
        return "user_form";
    }

    @PutMapping("/edit/{id}")
    public String updateUser( @ModelAttribute User user, @RequestParam(value = "roleStr", required = false) Set<String> roleStr) {
        user.setRole(userService.toSetRoles(roleStr));
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
