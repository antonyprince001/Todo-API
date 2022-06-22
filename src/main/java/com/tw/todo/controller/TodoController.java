package com.tw.todo.controller;

import com.tw.todo.entity.Todo;
import com.tw.todo.exception.InvalidTodoException;
import com.tw.todo.exception.TodoNotFoundException;
import com.tw.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class TodoController {

    @Autowired
    TodoService todoService;

    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> getAllTodos() {
        return new ResponseEntity<>(todoService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable(value = "id") Long id) {
        try {
            return new ResponseEntity<>(todoService.findById(id), HttpStatus.OK);
        } catch (TodoNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/todos")
    public ResponseEntity<Todo> create(@RequestBody Todo todo) {
        try {
            return new ResponseEntity<>(todoService.save(todo), HttpStatus.CREATED);
        } catch (InvalidTodoException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
