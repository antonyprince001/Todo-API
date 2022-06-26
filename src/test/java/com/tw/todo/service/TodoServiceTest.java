package com.tw.todo.service;

import com.tw.todo.entity.Todo;
import com.tw.todo.exception.InvalidTodoException;
import com.tw.todo.exception.TodoNotFoundException;
import com.tw.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    Todo todo;

    long id;

    @BeforeEach
    void setUp() {
        id = 1L;
        todo = new Todo("NEEV", false, false);
    }

    @Test
    void shouldGetAllTodos() {
        List<Todo> todos = new ArrayList<>();
        todos.add(new Todo("NEEV", false, false));
        todos.add(new Todo("TWARAN", true, false));
        when(todoRepository.findAll()).thenReturn(todos);

        List<Todo> fetchedTodos = todoService.findAll();

        assertThat(fetchedTodos.get(0).getDescription(), is("NEEV"));
        assertThat(fetchedTodos.get(0).isCompleted(), is(false));
        assertThat(fetchedTodos.get(1).getDescription(), is("TWARAN"));
        assertThat(fetchedTodos.get(1).isCompleted(), is(true));
    }

    @Test
    void shouldGetTodoById() throws TodoNotFoundException {
        todo.setId(id);
        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));

        Todo todoFetched = todoService.findById(todo.getId());

        assertThat(todoFetched.getDescription(), is("NEEV"));
        assertThat(todoFetched.isCompleted(), is(false));
    }

    @Test
    void shouldThrowExceptionIfByTodoNotFoundById() {
        when(todoRepository.findById(id)).thenReturn(null);

        assertThrows(TodoNotFoundException.class, () -> todoService.findById(0L));
    }

    @Test
    void shouldSaveATodo() throws InvalidTodoException {
        when(todoRepository.save(todo)).thenReturn(todo);

        Todo savedTodo = todoService.save(todo);

        assertThat(savedTodo.getDescription(), is("NEEV"));
        assertThat(savedTodo.isCompleted(), is(false));
    }

    @Test
    void shouldThrowExceptionIfTodoToBeSavedInvalid() {
        Todo todoOne = new Todo(null, false, false);
        Todo todoTwo = new Todo("", false, false);
        Todo todoThree = new Todo("     ", false, false);

        assertThrows(InvalidTodoException.class, () -> todoService.save(todoOne));
        assertThrows(InvalidTodoException.class, () -> todoService.save(todoTwo));
        assertThrows(InvalidTodoException.class, () -> todoService.save(todoThree));
    }

    @Test
    void shouldUpdateATodoById() throws InvalidTodoException, TodoNotFoundException {
        todo.setId(id);
        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));
        when(todoRepository.save(todo)).thenReturn(todo);

        Todo savedTodo = todoService.updateById(todo);

        assertThat(savedTodo.getDescription(), is("NEEV"));
        assertThat(savedTodo.isCompleted(), is(false));
    }

    @Test
    void shouldThrowExceptionIfTodoToBeUpdatedNotFound() {
        when(todoRepository.findById(id)).thenReturn(null);

        assertThrows(TodoNotFoundException.class, () -> todoService.updateById(todo));
    }

    @Test
    void shouldThrowExceptionIfTodoDataToBeUpdatedInvalid() {
        Todo todoOne = new Todo(null, false, false);
        todoOne.setId(0L);
        Todo todoTwo = new Todo("", false, false);
        todoTwo.setId(1L);
        Todo todoThree = new Todo("     ", false, false);
        todoThree.setId(2L);
        when(todoRepository.findById(0L)).thenReturn(Optional.of(todoOne));
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todoTwo));
        when(todoRepository.findById(2L)).thenReturn(Optional.of(todoThree));


        assertThrows(InvalidTodoException.class, () -> todoService.updateById(todoOne));
        assertThrows(InvalidTodoException.class, () -> todoService.updateById(todoTwo));
        assertThrows(InvalidTodoException.class, () -> todoService.updateById(todoThree));
    }

    @Test
    void shouldDeleteATodoById() throws TodoNotFoundException {
        todo.setId(id);
        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));

        Todo deletedTodo = todoService.deleteById(id);

        assertThat(deletedTodo.getDescription(), is("NEEV"));
        assertThat(deletedTodo.isCompleted(), is(false));
    }

    @Test
    void shouldThrowExceptionIfTodoToBeDeletedNotFound() {
        todo.setId(id);
        when(todoRepository.findById(id)).thenReturn(null);

        assertThrows(TodoNotFoundException.class, () -> todoService.deleteById(id));
    }
}