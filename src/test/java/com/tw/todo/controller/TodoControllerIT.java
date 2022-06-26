package com.tw.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.todo.entity.Todo;
import com.tw.todo.exception.InvalidTodoException;
import com.tw.todo.exception.TodoNotFoundException;
import com.tw.todo.repository.TodoRepository;
import com.tw.todo.service.TodoService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TodoControllerIT {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    MockMvc mockMvc;

    private Todo todo;

    @BeforeEach
    void setUp() {
        todo = new Todo("NEEV", false);
        todoRepository.deleteAll();
    }

    @Test
    public void shouldGetAllTodos() throws Exception {
        Todo todoOne = new Todo("NEEV", false);
        Todo todoTwo = new Todo("TWARAN", true);
        todoRepository.save(todoOne);
        todoRepository.save(todoTwo);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/todos")
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(jsonPath("$", hasSize(2)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void shouldGetTodoForId() throws Exception, TodoNotFoundException {
        Todo savedTodo = todoRepository.save(todo);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/todos/" + savedTodo.getId())
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.description", Matchers.is("NEEV")))
                .andExpect(jsonPath("$.completed", Matchers.is(false)));
    }

    @Test
    void shouldCreateATodo() throws Exception, InvalidTodoException {
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
    void shouldUpdateATodo() throws TodoNotFoundException, InvalidTodoException, Exception {
        Todo savedTodo = todoRepository.save(todo);
        todo.setCompleted(true);
        ObjectMapper objectMapper = new ObjectMapper();
        String todoJSON = objectMapper.writeValueAsString(todo);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/todos/"+savedTodo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(todoJSON)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("NEEV"))
                .andExpect(jsonPath("$.completed").value(true));
    }
}
