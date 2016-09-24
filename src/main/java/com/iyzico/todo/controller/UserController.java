package com.iyzico.todo.controller;

import com.iyzico.todo.exception.ThereIsSuchLikeUserException;
import com.iyzico.todo.model.User;
import com.iyzico.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by heval on 21.09.2016.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public String addUser(Model model, @ModelAttribute(value = "user") @Valid User user, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            try {
                userService.addUser(user);
                model.addAttribute("success", true);
            } catch (ThereIsSuchLikeUserException e) {
                model.addAttribute("success", false);
            }
        }
        return "index";
    }
}
