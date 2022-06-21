package com.tw.todo.service;

import com.tw.todo.entity.Todo;
import com.tw.todo.exception.TodoNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    public List<Todo> findAll() {
        return null;
    }

    public Todo findById(long id) throws TodoNotFoundException {
        return null;
    }
}
