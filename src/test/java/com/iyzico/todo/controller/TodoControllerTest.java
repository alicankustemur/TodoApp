package com.iyzico.todo.controller;

import com.iyzico.todo.message.Message;
import com.iyzico.todo.model.LongWrapper;
import com.iyzico.todo.model.Todo;
import com.iyzico.todo.service.TodoService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by heval on 24.09.2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoControllerTest {

    private static final int EXPECTED_TODO_LIST_SIZE = 2;
    private MockMvc mockMvc;

    @Mock
    private TodoService todoService;

    @InjectMocks
    @Autowired
    private TodoController todoController;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testIndexPage() throws Exception {
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("userId", new LongWrapper(1L));
        Optional<List<Todo>> todoList = Optional.of(Lists.newArrayList(new Todo(), new Todo()));
        when(todoService.getAllTodosByUserId(1L)).thenReturn(todoList);

        mockMvc.perform(get("/todo").session(mockHttpSession))
                .andExpect(status().isOk())
                .andExpect(view().name("todo"))
                .andExpect(handler().handlerType(TodoController.class))
                .andExpect(handler().methodName("index"))
                .andExpect(model().attribute("todoList", todoList.orElse(Collections.emptyList())))
                .andExpect(model().attribute("todoListSize", EXPECTED_TODO_LIST_SIZE))
                .andExpect(model().attribute("todoMessage", Message.DEFAULT_MESSAGE))
                .andReturn();
    }

    @Test
    public void testUpdatable() throws Exception {
        Optional<List<Todo>> todoList = Optional.of(Lists.newArrayList(new Todo(), new Todo()));
        long userId = 1L;
        when(todoService.getAllTodosByUserId(userId)).thenReturn(todoList);
        when(todoService.findById(userId)).thenReturn(Optional.of(new Todo()));

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("userId", userId);

        mockMvc.perform(get("/updatable/{id}", userId)
                .session(mockHttpSession))
                .andExpect(view().name("redirect:/todo"))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(TodoController.class))
                .andExpect(handler().methodName("updatable"))
                .andReturn();
    }

    @Test
    public void testSearch() throws Exception {
        Optional<List<Todo>> todoList = Optional.of(Lists.newArrayList(new Todo(), new Todo()));

        when(todoService.findByTodoNameContaining("test")).thenReturn(todoList);

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("userId", new LongWrapper(1L));

        mockMvc.perform(get("/search")
                .session(mockHttpSession)
                .param("keyword", "test"))
                .andExpect(status().isOk())
                .andExpect(view().name("todo"))
                .andExpect(handler().handlerType(TodoController.class))
                .andExpect(handler().methodName("search"))
                .andExpect(model().attribute("todoMessage", Message.SEARCH_MESSAGE))
                .andReturn();

    }

    @Test
    public void testAddTodo() throws Exception {
        mockMvc.perform(post("/addTodo")
                .param("todoName", "test-todo"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/todo"))
                .andExpect(handler().handlerType(TodoController.class))
                .andExpect(handler().methodName("addTodo"))
                .andReturn();
    }

    @Test
    public void testUpdateTodo() throws Exception {
        mockMvc.perform(post("/updateTodo"))
                .andExpect(view().name("redirect:/todo"))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(TodoController.class))
                .andExpect(handler().methodName("updateTodo"))
                .andReturn();
    }

    @Test
    public void testDeleteTodo() throws Exception {
        mockMvc.perform(get("/deleteTodo/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/todo"))
                .andExpect(handler().handlerType(TodoController.class))
                .andExpect(handler().methodName("deleteTodo"))
                .andExpect(view().name("redirect:/todo"))
                .andReturn();
    }

    @Test
    public void testDeleteAllTodos() throws Exception {
        mockMvc.perform(get("/deleteAllTodos"))
                .andExpect(view().name("redirect:/todo"))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(TodoController.class))
                .andExpect(handler().methodName("deleteAllTodos"))
                .andExpect(view().name("redirect:/todo"))
                .andReturn();
    }
}
