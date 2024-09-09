package com.example.TaskManager.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.TaskManager.datamodel.Task;
import com.example.TaskManager.datamodel.TaskStatus;
import com.example.TaskManager.datamodel.User;
import com.example.TaskManager.repositories.TaskRepository;
import com.example.TaskManager.repositories.UserRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userService; // To get logged-in user details

    public Task createTask(Task task, String email) {
        User creator = userService.findByEmail(email).orElse(null);
        task.setCreatedBy(creator);
        task.setStatus(TaskStatus.CREATED);
        task.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }
    
    @Transactional(readOnly = true)
    public Task getTask(Long id, String email) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Task not found"));
        if (!task.getAssignee().getUsername().equals(email) && !task.getCreatedBy().getUsername().equals(email)) {
            throw new AccessDeniedException("403 - Access Denied");
        }
        return task;
    }

    public List<Task> getTasksForUser(String email) {
        User user = userService.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Unable to find the User"));
        return taskRepository.findByAssignee(user);
    }

    public Task updateTask(Long id, Task updatedTask, String email) {
        Task task = getTask(id, email);
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setDueDate(updatedTask.getDueDate());
        task.setUpdatedAt(LocalDateTime.now());
        task.setUpdatedBy(userService.findByEmail(email).orElse(null));
        return taskRepository.save(task);
    }

    public void deleteTask(Long id, String email) {
        Task task = getTask(id, email);
        taskRepository.delete(task);
    }
}