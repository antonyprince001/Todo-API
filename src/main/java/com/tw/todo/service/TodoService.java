package com.tw.todo.service;

import com.tw.todo.entity.Todo;
import com.tw.todo.exception.InvalidTodoException;
import com.tw.todo.exception.TodoNotFoundException;
import com.tw.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    public Todo findById(long id) throws TodoNotFoundException {
        return todoRepository.findById(id).orElse(null);
    }

    public Todo save(Todo todo) throws InvalidTodoException {
        return null;
    }

    public Todo deleteById(Long id) throws TodoNotFoundException{
        return null;
    }
}
