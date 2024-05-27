package com.example.todoapp.controller;

import com.example.todoapp.domain.ToDo;
import com.example.todoapp.repository.ToDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TodoController {
    private final ToDoRepository toDoRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<ToDo> todos = toDoRepository.findAllByOrderByOrderingAsc();
        model.addAttribute("todos", todos);
        return "todos";
    }

    @PostMapping("/addTodo")
    public String addTodo(@RequestParam("todo") String todo) {
        List<ToDo> todos = toDoRepository.findAll();
        int maxOrdering = todos.stream().mapToInt(ToDo::getOrdering).max().orElse(0); // 현재 가장 큰 ordering 값 가져오기
        ToDo toDo = new ToDo();
        toDo.setTodo(todo);
        toDo.setOrdering(maxOrdering + 1); // 새로운 ToDo의 ordering 값을 현재 최대값보다 1 크게 설정
        toDoRepository.save(toDo);
        return "redirect:/";
    }


    @PostMapping("/deleteTodo")
    public String deleteTodo(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        toDoRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/editTodo")
    public String editTodoForm(@RequestParam("id") Long id, Model model) {
        ToDo toDo = toDoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid todo ID:" + id));
        model.addAttribute("todo", toDo);
        return "editTodo";
    }

    @PostMapping("/updateTodo")
    public String updateTodo(@RequestParam("id") Long id, @RequestParam("todo") String newTodo, RedirectAttributes redirectAttributes) {
        ToDo toDo = toDoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid todo ID:" + id));
        toDo.setTodo(newTodo);
        toDoRepository.save(toDo);
        redirectAttributes.addFlashAttribute("message", "Todo 업데이트에 성공하였습니다.");
        return "redirect:/";
    }
    @PostMapping("/moveUpTodo")
    public String moveUpTodo(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        ToDo todo = toDoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid todo ID:" + id));
        ToDo previousTodo = toDoRepository.findTopByOrderingLessThanOrderByOrderingDesc(todo.getOrdering());
        if (previousTodo != null) {
            int tempOrdering = todo.getOrdering();
            todo.setOrdering(previousTodo.getOrdering());
            previousTodo.setOrdering(tempOrdering);
            toDoRepository.saveAll(Arrays.asList(todo, previousTodo));
        }
        return "redirect:/";
    }

    @PostMapping("/moveDownTodo")
    public String moveDownTodo(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        ToDo todo = toDoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid todo ID:" + id));
        ToDo nextTodo = toDoRepository.findTopByOrderingGreaterThanOrderByOrderingAsc(todo.getOrdering());
        if (nextTodo != null) {
            int tempOrdering = todo.getOrdering();
            todo.setOrdering(nextTodo.getOrdering());
            nextTodo.setOrdering(tempOrdering);
            toDoRepository.saveAll(Arrays.asList(todo, nextTodo));
        }
        return "redirect:/";
    }

    private int findTodoIndexById(List<ToDo> todos, Long id) {
        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).getId().equals(id)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Invalid todo ID:" + id);
    }
}