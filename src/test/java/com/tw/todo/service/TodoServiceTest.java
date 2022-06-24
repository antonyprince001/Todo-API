package com.tw.todo.service;

import com.tw.todo.entity.Todo;
import com.tw.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    void shouldGetAllTodos() {
        List<Todo> todos = new ArrayList<>();
        todos.add(new Todo("NEEV", false));
        todos.add(new Todo("TWARAN", true));
        when(todoRepository.findAll()).thenReturn(todos);

        List<Todo> fetchedTodos = todoService.findAll();

        assertThat(fetchedTodos.get(0).getDescription(), is("NEEV"));
        assertThat(fetchedTodos.get(0).isCompleted(), is(false));
        assertThat(fetchedTodos.get(1).getDescription(), is("TWARAN"));
        assertThat(fetchedTodos.get(1).isCompleted(), is(true));
    }
}