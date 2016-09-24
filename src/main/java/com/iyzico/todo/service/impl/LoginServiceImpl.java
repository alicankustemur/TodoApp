package com.iyzico.todo.service.impl;

import com.iyzico.todo.model.User;
import com.iyzico.todo.repository.UserRepository;
import com.iyzico.todo.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Optional;

/**
 * Created by heval on 22.09.2016.
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValidUser(User user) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(user.getUsername()));
        User gettingUser = optionalUser.orElse(new User());
        return passwordIsTrue(user, gettingUser);
    }

    private boolean passwordIsTrue(User user, User gettingUser) {
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        return user.getPassword().equals(gettingUser.getPassword());
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
