package com.iyzico.todo.service;

import com.iyzico.todo.model.Todo;
import com.iyzico.todo.repository.TodoRepository;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by heval on 23.09.2016.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    @Autowired
    private TodoService todoService;

    @Test
    public void givenTodoWhenAddThenShouldReturnTodo() throws Exception {
        Todo todo = getTodo();
        when(todoRepository.save(todo)).thenReturn(todo);
        Todo gettingTodo = todoService.addTodo(todo).orElseGet(null);
        assertThat(gettingTodo.getId(), is(equalTo(gettingTodo.getId())));
        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    public void givenTodoNameWhenFoundThenFoundThenShouldBeNotEmpty() throws Exception {
        Todo todo = getTodo();
        when(todoRepository.findByTodoNameContaining(todo.getTodoName())).thenReturn(Lists.newArrayList(todo));
        List<Todo> foundTodoList = todoService.findByTodoNameContaining(todo.getTodoName()).orElse(Lists.emptyList());
        assertThat(foundTodoList.isEmpty(), is(equalTo(false)));
        verify(todoRepository, times(1)).findByTodoNameContaining(todo.getTodoName());
    }

    @Test
    public void givenUserWhenUpdateThenShouldChange() throws Exception {
        Todo todo = getTodo();
        Todo newTodo = new Todo();
        newTodo.setDate(todo.getDate());
        newTodo.setUserId(todo.getUserId());
        newTodo.setTodoName("updated");
        newTodo.setId(todo.getId());

        when(todoRepository.findById(todo.getId())).thenReturn(todo);
        when(todoRepository.save(todo)).thenReturn(newTodo);

        Todo updatedUser = todoService.updateTodo(todo.getId(), todo.getTodoName()).orElse(new Todo());

        assertThat(newTodo.getTodoName(), is(equalTo(updatedUser.getTodoName())));

        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    public void givenTodoIdWhenDeleteThenNotFound() throws Exception {
        Todo todo = getTodo();

        when(todoRepository.findByTodoNameContaining(todo.getTodoName())).thenReturn(Lists.newArrayList(todo));

        List<Todo> todoList = todoService.findByTodoNameContaining(todo.getTodoName()).orElse(Lists.emptyList());
        Todo gettingTodo = todoList.get(0);
        assertThat(gettingTodo.getTodoName(), is(equalTo(todo.getTodoName())));

        todoService.deleteTodo(todo.getId());

        when(todoRepository.findByTodoNameContaining(todo.getTodoName())).thenReturn(Lists.emptyList());

        List<Todo> newTodoList = todoService.findByTodoNameContaining(gettingTodo.getTodoName()).orElse(Lists.emptyList());

        assertThat(newTodoList.isEmpty(), is(equalTo(true)));
        verify(todoRepository, times(2)).findByTodoNameContaining(todo.getTodoName());
    }

    @Test
    public void whenCallDeleteAllThenTodoListShouldBeIsEmpty() throws Exception {
        Todo todo = getTodo();
        when(todoRepository.findAllByUserId(todo.getUserId())).thenReturn(Lists.newArrayList(todo, new Todo()));

        todoService.deleteAllTodos(todo.getUserId());

        when(todoRepository.findAllByUserId(todo.getUserId())).thenReturn(Lists.emptyList());

        List<Todo> todoList = todoService.getAllTodosByUserId(todo.getUserId()).orElse(Lists.emptyList());

        assertThat(todoList.isEmpty(), is(equalTo(true)));

        verify(todoRepository, times(1)).findAllByUserId(todo.getUserId());
    }

    @Test
    public void givenIdWhenFoundThenReturnTodo() throws Exception {
        Todo todo = getTodo();
        when(todoRepository.findById(1L)).thenReturn(todo);
        Todo gettingTodo = todoService.findById(1L).orElse(new Todo());
        assertThat(todo.getId(), is(equalTo(gettingTodo.getId())));
        verify(todoRepository, times(1)).findById(1L);
    }

    private Todo getTodo() {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setUserId(1L);
        todo.setTodoName("New Todo");
        todo.setDate(Calendar.getInstance().getTime());
        return todo;
    }
}
