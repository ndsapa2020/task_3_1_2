package com.jm.springBootstrap.controllers;

import com.jm.springBootstrap.model.Role;
import com.jm.springBootstrap.model.User;
import com.jm.springBootstrap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String startAdmin(ModelMap modelMap, Principal principal) {
        Iterable<User> userList = userService.findAll();
        Set<Role> rolesList = Role.getRolesSet();
        modelMap.addAttribute("usersList", userList);
        modelMap.addAttribute("roles", rolesList);
        return "admin";
    }

    @PostMapping("/create")
    public String createUser(HttpServletRequest request,
                             @RequestParam(name = "isAdmin", required = false) boolean isAdmin,
                             @RequestParam(name = "isUser", required = false) boolean isUser){
        User user =  new User();
        user.setLogin(request.getParameter("newName").trim());
        user.setLastName(request.getParameter("newLastName").trim());
        user.setAge(Integer.parseInt(request.getParameter("newAge")));
        user.setEmail(request.getParameter("newEmail").trim());
        user.setPassword(request.getParameter("newPassword").trim());
        Set<Role> rolesToAdd = new HashSet<>();
        if (isUser) {
            rolesToAdd.add(new Role(1L, "ROLE_USER"));
        }
        if (isAdmin) {
            rolesToAdd.add(new Role(2L, "ROLE_ADMIN"));
        }
        user.setRoles(rolesToAdd);
        userService.saveUser(user);
        return  "redirect:/admin";
    }

    @PostMapping(value = "/edit")
    public String saveUserMapping(ModelMap model,
                           @RequestParam Long idEdit,
                           @RequestParam Integer ageEdit,
                           @RequestParam String firstNameEdit,
                           @RequestParam String lastNameEdit,
                           @RequestParam String passwordEdit,
                           @RequestParam String emailEdit,
                           @RequestParam(value = "newRoles" , required = false) long[] roles) {
        User user = userService.findById(idEdit);
        user.setPassword(passwordEdit);
        user.setLogin(firstNameEdit);
        user.setLastName(lastNameEdit);
        user.setEmail(emailEdit);
        user.setAge(ageEdit);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/delete")
    public String deleteUserForm(@RequestParam(name = "id", defaultValue = "1") long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }
}
