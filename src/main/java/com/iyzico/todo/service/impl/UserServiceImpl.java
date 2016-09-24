package com.iyzico.todo.service.impl;

import com.iyzico.todo.exception.ThereIsSuchLikeUserException;
import com.iyzico.todo.model.User;
import com.iyzico.todo.repository.UserRepository;
import com.iyzico.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Optional;

/**
 * Created by heval on 21.09.2016.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public Optional<User> addUser(User user) {
        if (isThereSuchLikeUser(user)) {
            String hashingPassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
            user.setPassword(hashingPassword);
            return Optional.of(userRepository.save(user));
        }
        throw new ThereIsSuchLikeUserException();
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public Optional<User> updateUser(User user) {
        return Optional.of(userRepository.save(user));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    private boolean isThereSuchLikeUser(User user) {
        return userRepository.findByEmail(user.getEmail()) == null && userRepository.findByUsername(user.getUsername()) == null;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
