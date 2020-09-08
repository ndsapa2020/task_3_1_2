package com.jm.springBootstrap.controllers;

import com.jm.springBootstrap.model.User;
import com.jm.springBootstrap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("user")
    public ModelAndView index(Principal principal, ModelAndView mav) {
        User user = userService.getUserByEmail(principal.getName());
        mav.addObject("user", user);
        mav.setViewName("/user");
        return mav;
    }
}
