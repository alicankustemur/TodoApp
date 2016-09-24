package com.iyzico.todo.service;

import com.iyzico.todo.model.User;

import java.util.Optional;

/**
 * Created by heval on 21.09.2016.
 */
public interface UserService {
    Optional<User> addUser(User user);

    void deleteUser(User user);

    Optional<User> updateUser(User user);

    Optional<User> findByUsername(String username);
}
