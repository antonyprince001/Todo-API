package com.tw.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.todo.entity.Todo;
import com.tw.todo.exception.InvalidTodoException;
import com.tw.todo.exception.TodoNotFoundException;
import com.tw.todo.service.TodoService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class TodoControllerTest {

    @MockBean
    private TodoService todoService;

    @Autowired
    MockMvc mockMvc;

    Todo todo;

    long id;

    @BeforeEach
    void setUp() {
        id = 1L;
        todo =  new Todo("NEEV", false);
    }

    @Test
    void shouldReturnAllTodos() throws Exception {
        List<Todo> todos = new ArrayList<>();
        todos.add(new Todo("NEEV", false));
        todos.add(new Todo("TWARAN", true));
        when(todoService.findAll()).thenReturn(todos);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/todos")
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(jsonPath("$", hasSize(2)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldReturnTodoForId() throws Exception, TodoNotFoundException {
        when(todoService.findById(id)).thenReturn(todo);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.description", Matchers.is("NEEV")))
                .andExpect(jsonPath("$.completed", Matchers.is(false)));
    }

    @Test
    void shouldReturnStatus404IfTodoNotFoundForId() throws Exception, TodoNotFoundException {
        when(todoService.findById(id)).thenThrow(new TodoNotFoundException());

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/todos/1"));

        result.andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateATodo() throws Exception, InvalidTodoException {
        when(todoService.save(ArgumentMatchers.any(Todo.class))).thenReturn(todo);
        ObjectMapper objectMapper = new ObjectMapper();
        String todoJSON = objectMapper.writeValueAsString(todo);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(todoJSON)
        );

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("NEEV"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void shouldNotCreateATodoIfRequestBodyIsNotValid() throws Exception, InvalidTodoException {
        Todo todo = new Todo(null, false);
        when(todoService.save(ArgumentMatchers.any(Todo.class))).thenThrow(new InvalidTodoException());
        ObjectMapper objectMapper = new ObjectMapper();
        String todoJSON = objectMapper.writeValueAsString(todo);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(todoJSON)
        );

        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateATodo() throws TodoNotFoundException, InvalidTodoException, Exception {
        when(todoService.updateById(ArgumentMatchers.any(Todo.class))).thenReturn(todo);
        ObjectMapper objectMapper = new ObjectMapper();
        String bookJSON = objectMapper.writeValueAsString(todo);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/todos/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJSON)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("NEEV"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void shouldReturnStatus404IfTodoNotFoundForUpdate() throws TodoNotFoundException, InvalidTodoException, Exception {
        when(todoService.updateById(ArgumentMatchers.any(Todo.class))).thenThrow(new TodoNotFoundException());
        ObjectMapper objectMapper = new ObjectMapper();
        String bookJSON = objectMapper.writeValueAsString(todo);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/todos/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJSON)
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    void shouldNotUpdateATodoIfRequestBodyIsNotValid() throws TodoNotFoundException, InvalidTodoException, Exception {
        Todo todo = new Todo(null, true);
        when(todoService.updateById(ArgumentMatchers.any(Todo.class))).thenThrow(new InvalidTodoException());
        ObjectMapper objectMapper = new ObjectMapper();
        String bookJSON = objectMapper.writeValueAsString(todo);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/todos/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJSON)
        );

        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteATodoById() throws Exception, TodoNotFoundException {
        when(todoService.deleteById(id)).thenReturn(todo);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .delete("/todos/1"));

        result.andExpect(status().isOk());
    }

    @Test
    void shouldReturnStatus404IfTodoNotFoundForDelete() throws Exception, TodoNotFoundException {
        when(todoService.deleteById(id)).thenThrow(new TodoNotFoundException());

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .delete("/todos/1"));

        result.andExpect(status().isNotFound());
    }

}