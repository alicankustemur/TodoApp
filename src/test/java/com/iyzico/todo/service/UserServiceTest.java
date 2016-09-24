package com.iyzico.todo.service;

import com.iyzico.todo.exception.ThereIsSuchLikeUserException;
import com.iyzico.todo.model.User;
import com.iyzico.todo.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    private static final String EMAIL = "hevalberknevruz@gmail.com";
    private static final String USERNAME = "hevalberk";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    @Autowired
    private UserService userService;

    @Test
    public void givenUserWhenThereIsNotSuchLikeUserThenShouldReturnTodo() throws Exception {
        User user = getUser();

        when(userRepository.findByUsername(USERNAME)).thenReturn(null);
        when(userRepository.findByEmail(EMAIL)).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        User gettingUser = userService.addUser(user).orElse(new User());

        assertThat(gettingUser.getId(), is(equalTo(user.getId())));

        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).findByUsername(USERNAME);
        verify(userRepository, times(1)).findByEmail(EMAIL);
    }

    @Test(expected = ThereIsSuchLikeUserException.class)
    public void givenUserWhenThereIsSuchLikeUserThenShouldThrowException() throws Exception {
        User user = getUser();

        when(userRepository.findByUsername(USERNAME)).thenReturn(user);
        when(userRepository.findByEmail(EMAIL)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        userService.addUser(user).orElse(new User());
    }

    @Test
    public void givenUserWhenDeleteUserThenNotFound() throws Exception {
        User user = getUser();

        when(userRepository.findByUsername(USERNAME)).thenReturn(null);
        when(userRepository.findByEmail(EMAIL)).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        User gettingUser = userService.addUser(user).orElse(new User());

        userService.deleteUser(gettingUser);

        User deletedUser = userService.findByUsername(gettingUser.getUsername()).orElse(null);
        assertThat(deletedUser, nullValue());

        verify(userRepository, times(2)).findByUsername(USERNAME);
        verify(userRepository, times(1)).findByEmail(EMAIL);
        verify(userRepository, times(1)).delete(gettingUser);
    }

    @Test
    public void givenUserWhenUpdateThenShouldUpdategivenUserWhenUpdateThenUpdated() throws Exception {
        User user = getUser();

        User newUser = new User();
        newUser.setPassword(user.getPassword());
        newUser.setUsername("updated");
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setEmail(user.getEmail());
        newUser.setId(user.getId());

        when(userRepository.save(user)).thenReturn(newUser);

        User updatedUser = userService.updateUser(user).orElse(new User());
        assertThat(newUser.getUsername(), is(equalTo(updatedUser.getUsername())));

        verify(userRepository, times(1)).save(user);
    }

    private User getUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);
        user.setPassword("123456");
        user.setPhoneNumber("05374434433");
        return user;
    }
}
