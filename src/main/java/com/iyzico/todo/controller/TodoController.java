package com.iyzico.todo.controller;

import com.iyzico.todo.message.Message;
import com.iyzico.todo.model.LongWrapper;
import com.iyzico.todo.model.Todo;
import com.iyzico.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by heval on 21.09.2016.
 */
@SessionAttributes("userId")
@Controller
public class TodoController {

    @Autowired
    private TodoService todoService;
    private Long userId;

    @RequestMapping(value = "/todo", method = RequestMethod.GET)
    public String index(@ModelAttribute("userId") LongWrapper longWrapper, Model model) {
        model.addAttribute("todo", new Todo());
        userId = longWrapper.getUserId();
        List<Todo> todoList = todoService.getAllTodosByUserId(userId).orElse(Collections.emptyList());
        model.addAttribute("todoList", todoList);
        model.addAttribute("todoListSize", todoList.size());
        model.addAttribute("todoMessage", Message.DEFAULT_MESSAGE);
        return "todo";
    }


    @RequestMapping(value = "/addTodo", method = RequestMethod.POST)
    public String addTodo(@ModelAttribute("todo") @Valid Todo todo, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            todo.setUserId(userId);
            todoService.addTodo(todo);
        }
        return "redirect:/todo";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@ModelAttribute("userId") LongWrapper longWrapper, @RequestParam("keyword") String keyword, Model model) {
        List<Todo> foundedTodoList = todoService.findByTodoNameContaining(keyword).orElse(Collections.emptyList())
                .stream()
                .filter(e -> Objects.equals(e.getUserId(), longWrapper.getUserId()))
                .collect(Collectors.toList());

        model.addAttribute("todoList", foundedTodoList);
        model.addAttribute("todoListSize", foundedTodoList.size());
        model.addAttribute("todoMessage", Message.SEARCH_MESSAGE);
        return "todo";
    }

    @RequestMapping(value = "/updateTodo", method = RequestMethod.POST)
    public String updateTodo(@ModelAttribute("todo") Todo todo) {
        todoService.updateTodo(todo.getId(), todo.getTodoName());
        return "redirect:/todo";
    }

    @RequestMapping(value = "/deleteTodo/{id}", method = RequestMethod.GET)
    public String deleteTodo(@PathVariable("id") Long id) {
        todoService.deleteTodo(id);
        return "redirect:/todo";
    }

    @RequestMapping(value = "/deleteAllTodos", method = RequestMethod.GET)
    public String deleteAllTodos() {
        todoService.deleteAllTodos(userId);
        return "redirect:/todo";
    }

    @RequestMapping(value = "/updatable/{id}", method = RequestMethod.GET)
    public String updatable(@PathVariable("id") Long id, Model model, @ModelAttribute("userId") LongWrapper longWrapper) {
        Todo todo = todoService.findById(id).orElse(new Todo());
        todo.setUpdatable(true);
        List<Todo> todoList = todoService.getAllTodosByUserId(longWrapper.getUserId()).orElse(Collections.emptyList());
        model.addAttribute("todoList", todoList);
        model.addAttribute("todoListSize", todoList.size());
        model.addAttribute("todoMessage", Message.DEFAULT_MESSAGE);
        return "redirect:/todo";
    }
}
