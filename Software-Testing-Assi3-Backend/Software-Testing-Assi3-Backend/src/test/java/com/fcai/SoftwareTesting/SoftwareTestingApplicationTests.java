package com.fcai.SoftwareTesting;

import com.fcai.SoftwareTesting.todo.Todo;
import com.fcai.SoftwareTesting.todo.TodoController;
import com.fcai.SoftwareTesting.todo.TodoCreateRequest;
import com.fcai.SoftwareTesting.todo.TodoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class SoftwareTestingApplicationTests {
    @Mock
    TodoService mockService;
    @InjectMocks
    TodoController todoController;
    @Autowired
    private TodoService todoService;

	Todo[] todos = {
			new Todo("1", "test 1", "test 1", false),
			new Todo("2", "test 2", "test 2", true),
			new Todo("3", "test 3", "test 3", false),
			new Todo("4", "test 4", "test 4", false),
			new Todo("5", "test 4", "test 4", true)
	};
    //
    //CREATE
    //
    @Test
    public void testCreate(){//test create method with null TodoCreateRequest
        TodoCreateRequest testReq = null;
        Assertions.assertThrows(IllegalArgumentException.class, () -> todoService.create(testReq));
    }

    @Test
    public void testCreate2(){//test create method with empty title
        TodoCreateRequest testReq = new TodoCreateRequest("", "bla bla");
        Assertions.assertThrows(IllegalArgumentException.class, () -> todoService.create(testReq));
    }

    @Test
    public void testCreate3(){//test create method with valid title and description
        TodoCreateRequest testReq = new TodoCreateRequest("title", "description");
        Todo testTodo = todoService.create(testReq);
        Assertions.assertNotNull(testTodo);
    }

    @Test
    public void testCreate4(){//test create method with empty description
        TodoCreateRequest testReq = new TodoCreateRequest("title", "");
        Assertions.assertThrows(IllegalArgumentException.class, () -> todoService.create(testReq));
    }
    //
    //UPDATE
    //
    @Test
    public void testUpdate(){//test read with valid id
        todos[0].setCompleted(true);
        todoController = new TodoController(mockService);
        when(mockService.update("1", true)).thenReturn(todos[0]);
        ResponseEntity<Todo> res = todoController.update("1", true);
        assertTrue(res.getBody()==todos[0]);
        todos[0].setCompleted(false);
    }
    //
    //DELETE
    //
    @Test
    public void testDelete(){//test delete with valid id
        todoController = new TodoController(mockService);
        doNothing().when(mockService).delete("1");
        ResponseEntity<?> res = todoController.delete("1");
        assertEquals("200 OK", res.getStatusCode().toString());
    }
    //READ
    //
    @Test
    public void getToDoWithId_NotFound() {
        todoController = new TodoController(mockService);
        when(mockService.read(anyString())).thenThrow(IllegalArgumentException.class);
        ResponseEntity<Todo> s = todoController.read("4");
        assertEquals("400 BAD_REQUEST", s.getStatusCode().toString());
    }

    @Test
    public void getToDoWithId() {
        todoController = new TodoController(mockService);
        when(mockService.read(anyString())).thenReturn(todos[0]);
        ResponseEntity<Todo> s = todoController.read("1");
        assertTrue(s.getBody()==todos[0]);

    }

    @Test
    public void getToDoWithId_NullId() {
        todoController = new TodoController(mockService);
        when(mockService.read(null)).thenThrow(IllegalArgumentException.class);
        ResponseEntity<Todo> s = todoController.read(null);
        assertEquals("400 BAD_REQUEST", s.getStatusCode().toString());
    }

    @Test
    public void getToDoWithId_EmptyId() {
        todoController = new TodoController(mockService);
        when(mockService.read(anyString())).thenThrow(IllegalArgumentException.class);
        ResponseEntity<Todo> s = todoController.read("");
        assertEquals("400 BAD_REQUEST", s.getStatusCode().toString());
    }
    //
    //LIST
    //
    @Test
    public void listToDo_null() {
        todoController = new TodoController(mockService);
        when(mockService.list()).thenThrow(IllegalArgumentException.class);
        ResponseEntity<List<Todo>> s = todoController.list();
        assertEquals("400 BAD_REQUEST", s.getStatusCode().toString());
    }

    @Test
    public void listToDo() {
        todoController = new TodoController(mockService);
        when(mockService.list()).thenReturn(List.of(todos));
        ResponseEntity<List<Todo>> s = todoController.list();
        boolean allTrue = true;
		boolean found = false;
        if(s.getBody().size() != todos.length){
            allTrue = false;
        }
        for (Todo a :
                s.getBody()) {
			found = false;
            for(int i=0 ; i < todos.length;++i){
				if(a==todos[i]){
					found = true;
					break;
				}
			}
			if(!found){
				allTrue = false;
				break;
			}
        }
        assertTrue(allTrue);
    }
    //
    //LIST COMPLETED
    //
	@Test
    public void listCompleted() {
        todoController = new TodoController(mockService);
        when(mockService.listCompleted()).thenReturn(List.of(todos[2],todos[4]));
        ResponseEntity<List<Todo>> s = todoController.listCompleted();
        boolean allTrue = true;
		boolean found = false;
        if(s.getBody().size() != 2){
            allTrue = false;
        }
        for (Todo a :
                s.getBody()) {
			found = false;
			for (Todo todo : todos) {
				if (a == todo) {
					found = true;
					break;
				}
			}
			if(!found){
				allTrue = false;
				break;
			}
        }
        assertTrue(allTrue);
    }
	@Test
    public void listCompleted_null() {
        todoController = new TodoController(mockService);
        when(mockService.listCompleted()).thenThrow(IllegalArgumentException.class);
        ResponseEntity<List<Todo>> s = todoController.listCompleted();
		assertEquals("400 BAD_REQUEST",s.getStatusCode().toString());
    }
	@Test
    public void listCompleted_notFound() {
        todoController = new TodoController(mockService);
        when(mockService.listCompleted()).thenReturn(List.of());
        ResponseEntity<List<Todo>> s = todoController.listCompleted();
		assertEquals(List.of(),s.getBody());
    }
}
