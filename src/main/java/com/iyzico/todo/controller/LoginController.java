package com.iyzico.todo.controller;

import com.iyzico.todo.message.Message;
import com.iyzico.todo.model.LongWrapper;
import com.iyzico.todo.model.User;
import com.iyzico.todo.service.LoginService;
import com.iyzico.todo.service.UserService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Created by heval on 21.09.2016.
 */
@SessionAttributes({"login", "userId"})
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@ModelAttribute("user") User user, Model model) {
        if (loginFormIsEmpty(user)) {
            if (loginService.isValidUser(user)) {
                User gettingUser = userService.findByUsername(user.getUsername()).orElse(new User());
                model.addAttribute("login", true);
                model.addAttribute("userId", new LongWrapper(gettingUser.getId()));
                return "redirect:/todo";
            } else {
                model.addAttribute("loginMessage", Message.AUTHENTICATION_ERROR);
            }
        } else {
            model.addAttribute("loginMessage", Message.LOGIN_EMPTY_FORM_MESSAGE);
        }
        return "index";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().invalidate();
        return "redirect:/";
    }

    private boolean loginFormIsEmpty(@ModelAttribute("user") User user) {
        return !"".equals(user.getUsername())
                && !"".equals(user.getPassword());
    }
}
