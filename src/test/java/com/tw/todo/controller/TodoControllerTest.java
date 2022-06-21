package com.tw.todo.controller;

import com.tw.todo.entity.Todo;
import com.tw.todo.exception.TodoNotFoundException;
import com.tw.todo.service.TodoService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
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

    @Test
    void shouldReturnAllTodos() throws Exception {
        List<Todo> todos = new ArrayList<>();
        todos.add(new Todo("NEEV", false));
        todos.add(new Todo("TWARAN", true));

        when(todoService.findAll()).thenReturn(todos);

        mockMvc.perform(MockMvcRequestBuilders.get("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(jsonPath("$", hasSize(2)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldReturnTodoForId() throws Exception, TodoNotFoundException {
        Todo todo = new Todo("NEEV", false);

        when(todoService.findById(1L)).thenReturn(todo);

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.description", Matchers.is("NEEV")))
                .andExpect(jsonPath("$.completed", Matchers.is(false)));
    }

    @Test
    void shouldReturnStatus404IfTodoNotFoundForId() throws Exception, TodoNotFoundException {
        when(todoService.findById(1L)).thenThrow(new TodoNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/1"))
                .andExpect(status().isNotFound());
    }
}