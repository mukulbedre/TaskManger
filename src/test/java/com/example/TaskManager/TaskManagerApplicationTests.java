package com.example.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.TaskManager.datamodel.Task;
import com.example.TaskManager.datamodel.TaskStatus;
import com.example.TaskManager.repositories.TaskRepository;
import com.example.TaskManager.service.TaskService;

@SpringBootTest
class TaskManagerApplicationTests {

	 @Autowired
	    private TaskService taskService;

	    @MockBean
	    private TaskRepository taskRepository;

	    @Test
	    public void testCreateTask() {
	        Task task = new Task();
	        task.setTitle("Test Task");
	        task.setDescription("Test Description");

	        when(taskRepository.save(task)).thenReturn(task);

	        Task createdTask = taskService.createTask(task, "user");

	        assertEquals(task.getTitle(), createdTask.getTitle());
	        assertEquals(TaskStatus.CREATED, createdTask.getStatus());
	    }
}
