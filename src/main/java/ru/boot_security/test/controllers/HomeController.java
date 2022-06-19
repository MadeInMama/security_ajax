package ru.boot_security.test.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.boot_security.test.configs.PasswordEncoderWithDecoder;
import ru.boot_security.test.entities.User;
import ru.boot_security.test.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/home/")
public class HomeController {
    @Autowired
    UserService userService;
    @Autowired
    private PasswordEncoderWithDecoder passwordEncoder;

    @GetMapping("")
    public String profile(Model model, Principal principal) {
        User user = userService.findById(((User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getId());
        user.setPassword(passwordEncoder.decode(user.getPassword()));

        model.addAttribute("title", "Profile");
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping(value = "save")
    public String save(@ModelAttribute(name = "user") User user) {
        userService.save(user);

        return "redirect:/home/";
    }

    @GetMapping(value = "delete/{id}")
    public String delete(@PathVariable long id) {
        userService.deleteById(id);

        return "redirect:/logout";
    }
}