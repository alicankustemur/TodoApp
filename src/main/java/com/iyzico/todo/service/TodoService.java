package com.iyzico.todo.service;

import com.iyzico.todo.model.Todo;

import java.util.List;
import java.util.Optional;

/**
 * Created by heval on 21.09.2016.
 */
public interface TodoService {
    Optional<Todo> addTodo(Todo todo);

    Optional<List<Todo>> findByTodoNameContaining(String todoName);

    void deleteTodo(Long id);

    void deleteAllTodos(Long userId);

    Optional<List<Todo>> getAllTodosByUserId(Long userId);

    Optional<Todo> updateTodo(Long id, String todoName);

    Optional<Todo> findById(Long id);
}
