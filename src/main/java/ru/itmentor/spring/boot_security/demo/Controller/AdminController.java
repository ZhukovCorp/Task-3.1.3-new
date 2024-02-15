package ru.itmentor.spring.boot_security.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.RolesService;
import ru.itmentor.spring.boot_security.demo.service.UserService;
import org.springframework.ui.Model;

import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RolesService rolesService;

    @Autowired
    public AdminController(UserService userService, RolesService rolesService) {
        this.userService = userService;
        this.rolesService = rolesService;
    }

    @GetMapping("/signup")
    public String showSignUpForm(User user, Model model) {
        model.addAttribute("roles", rolesService.getAllRoles());
        return "add-user";
    }

    @PostMapping("/adduser")
    public String addUser(@ModelAttribute("user") User user,
                          @RequestParam("roles") Set<String> values) {
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("")
    public String showUserList(Model model) {
        model.addAttribute("users", userService.getAll());
        return "admin";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("roles", rolesService.getAllRoles());
        model.addAttribute("user", user);
        return "update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") int id, User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id, Model model) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
}
