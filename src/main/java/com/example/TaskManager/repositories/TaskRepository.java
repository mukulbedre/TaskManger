package com.example.TaskManager.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TaskManager.datamodel.Task;
import com.example.TaskManager.datamodel.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByAssignee(User assignee);
}