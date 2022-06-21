package com.tw.todo.controller;

import com.tw.todo.entity.Todo;
import com.tw.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class TodoController {

    @Autowired
    TodoService todoService;

    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> getAllTodos(){
        return new ResponseEntity<>(todoService.findAll(), HttpStatus.OK);
    }
}
