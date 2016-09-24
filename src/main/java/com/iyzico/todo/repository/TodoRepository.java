package com.iyzico.todo.repository;

import com.iyzico.todo.model.Todo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by heval on 21.09.2016.
 */
@Repository
public interface TodoRepository extends CrudRepository<Todo, Long> {
    List<Todo> findByTodoNameContaining(String todoName);

    Todo findById(Long id);

    List<Todo> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}
