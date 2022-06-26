package com.tw.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.todo.entity.Todo;
import com.tw.todo.repository.TodoRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
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
    public void shouldGetTodoForId() throws Exception {
        Todo savedTodo = todoRepository.save(todo);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/todos/" + savedTodo.getId())
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.description", Matchers.is("NEEV")))
                .andExpect(jsonPath("$.completed", Matchers.is(false)));
    }

    @Test
    void shouldCreateATodo() throws Exception {
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
    void shouldUpdateATodo() throws Exception {
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

    @Test
    void shouldDeleteATodoById() throws Exception {
        Todo savedTodo = todoRepository.save(todo);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .delete("/todos/"+savedTodo.getId()));

        result.andExpect(status().isOk());
    }
}
