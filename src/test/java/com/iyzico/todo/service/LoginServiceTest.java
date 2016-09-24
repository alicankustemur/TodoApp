package com.iyzico.todo.service;

import com.iyzico.todo.model.User;
import com.iyzico.todo.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by heval on 23.09.2016.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginServiceTest {
    private static final String USERNAME = "hevalberk";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    @Autowired
    private LoginService loginService;

    @Test
    public void givenUserThenIsValidThenTrue() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);
        user.setEmail("hevalberknevruz@gmail.com");
        user.setPassword("123456");
        user.setPhoneNumber("05374434433");

        when(userRepository.findByUsername(USERNAME)).thenReturn(user);
        assertThat(loginService.isValidUser(user), is(equalTo(true)));
        verify(userRepository, times(1)).findByUsername(USERNAME);
    }
}
