package com.iyzico.todo.service;

import com.iyzico.todo.model.User;

/**
 * Created by heval on 22.09.2016.
 */
public interface LoginService {
    boolean isValidUser(User user);
}
