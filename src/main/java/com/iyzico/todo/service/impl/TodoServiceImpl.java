package com.iyzico.todo.service.impl;

import com.iyzico.todo.model.Todo;
import com.iyzico.todo.repository.TodoRepository;
import com.iyzico.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * Created by heval on 21.09.2016.
 */
@Service
@Transactional
public class TodoServiceImpl implements TodoService {

    @Autowired
    TodoRepository todoRepository;

    @Override
    public Optional<Todo> addTodo(Todo todo) {
        todo.setDate(Calendar.getInstance().getTime());
        return Optional.of(todoRepository.save(todo));
    }

    @Override
    public Optional<List<Todo>> findByTodoNameContaining(String todoName) {
        return Optional.ofNullable(todoRepository.findByTodoNameContaining(todoName));
    }

    @Override
    public void deleteTodo(Long id) {
        todoRepository.delete(id);
    }

    @Override
    public void deleteAllTodos(Long userId) {
        todoRepository.deleteAllByUserId(userId);
    }

    @Override
    public Optional<List<Todo>> getAllTodosByUserId(Long userId) {
        List<Todo> todoList = new ArrayList<>();
        todoRepository.findAllByUserId(userId).iterator().forEachRemaining(todoList::add);
        return Optional.of(todoList);
    }

    @Override
    public Optional<Todo> updateTodo(Long id, String todoName) {
        Todo todo = todoRepository.findById(id);
        todo.setUpdatable(false);
        todo.setTodoName(todoName);
        return Optional.of(todoRepository.save(todo));
    }

    @Override
    public Optional<Todo> findById(Long id) {
        return Optional.ofNullable(todoRepository.findById(id));
    }

    public void setTodoRepository(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
}
