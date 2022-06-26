package com.tw.todo.repository;

import com.tw.todo.entity.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    Todo todo;

    @BeforeEach
    void setUp() {
        todo = new Todo("NEEV", false,false);
    }

    @Test
    void shouldSaveATodo() {
        Todo savedTodo = todoRepository.save(todo);

        assertThat(savedTodo.getDescription(), is("NEEV"));
        assertThat(savedTodo.isCompleted(), is(false));
    }

    @Test
    void shouldFetchAllTodos() {
        Todo todoOne = new Todo("NEEV", false,false);
        Todo todoTwo = new Todo("TWARAN", true, false);
        todoRepository.save(todoOne);
        todoRepository.save(todoTwo);

        List<Todo> fetchedTodos = todoRepository.findAll();

        assertThat(fetchedTodos.get(0).getDescription(), is("NEEV"));
        assertThat(fetchedTodos.get(0).isCompleted(), is(false));
        assertThat(fetchedTodos.get(1).getDescription(), is("TWARAN"));
        assertThat(fetchedTodos.get(1).isCompleted(), is(true));
    }

    @Test
    void shouldFetchATodoById() {
        Todo savedTodo = todoRepository.save(todo);

        Todo fetchedTodo = todoRepository.findById(savedTodo.getId()).get();

        assertThat(fetchedTodo.getDescription(), is("NEEV"));
        assertThat(fetchedTodo.isCompleted(), is(false));
    }

    @Test
    void shouldThrowExceptionIfTodoNotFoundForId() {
        assertThrows(NoSuchElementException.class, () -> todoRepository.findById(0L).get());
    }

    @Test
    void shouldDeleteATodoById() {
        Todo savedTodo = todoRepository.save(todo);

        todoRepository.deleteById(savedTodo.getId());

        assertThrows(NoSuchElementException.class, () -> todoRepository.findById(savedTodo.getId()).get());
    }

    @Test
    void shouldThrowExceptionIfTodoNotFoundForIdToBeDeleted() {
        assertThrows(EmptyResultDataAccessException.class, () -> todoRepository.deleteById(0L));
    }

}
