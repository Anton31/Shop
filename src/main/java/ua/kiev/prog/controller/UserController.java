package ua.kiev.prog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.kiev.prog.model.Role;
import ua.kiev.prog.model.User;
import ua.kiev.prog.repository.RoleRepository;
import ua.kiev.prog.security.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for user management
 */

@Controller
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;


    @RequestMapping(value = "/login_page", method = RequestMethod.GET)
    public String login(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("message", "Wrong login and password!");
            model.addAttribute("state", "alert alert-danger");
        }
        return "login_page";
    }

    @RequestMapping("/403")
    public String denied(Model model) {
        model.addAttribute("message", "Access denied");
        model.addAttribute("state", "alert alert-danger");
        return "login_page";
    }

    @RequestMapping("/register_page")
    public String register_page(Model model) {

        model.addAttribute("user", " Log in");

        return "register";
    }
    List<User> users = new ArrayList<>();
    List<Role> roles = new ArrayList<>();


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam String role, @RequestParam String username,
                           @RequestParam String password1, @RequestParam
                           String password2, Model model) {


        if (userService.findByUsername(username) == null && password1.equals(password2)) {

            if (userService.findRole(role) == null) {
                userService.saveRole(new Role(role));
                roles.add(userService.findRole(role));
                userService.save(new User(username, password2, roles));
            } else {
                roles.add(userService.findRole(role));
                userService.save(new User(username, password2, roles));
            }

            model.addAttribute("message", "registration success!");
            model.addAttribute("state", "alert alert-success");
            return "login_page";
        }
        if (users.size() > 0) {
            model.addAttribute("message", "user already exists!");
            model.addAttribute("state", "alert alert-danger");
            return "register";
        }
        if (!password1.equals(password2)) {
            model.addAttribute("message", "password are not matching");
            model.addAttribute("state", "alert alert-danger");
            return "register";
        }
        return "login_page";

    }

}
