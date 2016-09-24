package com.iyzico.todo.controller;

import com.iyzico.todo.message.Message;
import com.iyzico.todo.model.User;
import com.iyzico.todo.repository.UserRepository;
import com.iyzico.todo.service.LoginService;
import com.iyzico.todo.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by heval on 23.09.2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    @Autowired
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void givenUserWhenIsValidThenShouldLoggedId() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("heval");
        user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(userService.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/login")
                .param("username", "heval")
                .param("password", "123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/todo"))
                .andExpect(handler().handlerType(LoginController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(request().sessionAttribute("login", true))
                .andReturn();
    }

    @Test
    public void givenUserWhenIsNotValidThenShouldNotLoggedId() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("heval");
        user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(userService.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/login")
                .param("username", "wrongusername")
                .param("password", "wrongpass"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(handler().handlerType(LoginController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(model().attribute("loginMessage", Message.AUTHENTICATION_ERROR))
                .andReturn();
    }

    @Test
    public void givenUserWhenFormEmptyThenShouldSendMessage() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "")
                .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(handler().handlerType(LoginController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(model().attribute("loginMessage", Message.LOGIN_EMPTY_FORM_MESSAGE))
                .andReturn();
    }

    @Test
    public void testIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(handler().handlerType(LoginController.class))
                .andExpect(handler().methodName("index"))
                .andReturn();
    }
}
