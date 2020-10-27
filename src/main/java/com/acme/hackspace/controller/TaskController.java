package com.acme.hackspace.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.acme.hackspace.model.Task;
import com.acme.hackspace.resource.CreateTaskDto;
import com.acme.hackspace.resource.TaskResource;
import com.acme.hackspace.resource.UpdateTaskDto;
import com.acme.hackspace.service.TaskService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResource> createTask(@Valid @RequestBody CreateTaskDto createDto){
        TaskResource taskResource = convertToResource(taskService.createTask(convertCreateDtoToEntity(createDto)));
        return new ResponseEntity<TaskResource>(taskResource, HttpStatus.CREATED);
    }

    @GetMapping
    public Page<TaskResource> getAllTasks(Pageable pageable){
        Page<Task> tasks = taskService.getAllTasks(pageable);
        List<TaskResource> resources = tasks.getContent().stream().map(this::convertToResource).collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    @GetMapping("/{id}")
    public TaskResource getTaskById(@PathVariable Long id){
        return convertToResource(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    public TaskResource updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskDto updateDto){
        return convertToResource(taskService.updateTask(id, convertUpdateDtoToEntity(updateDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        //return ResponseEntity.ok().build();
    }

    private TaskResource convertToResource(Task entity){
        return mapper.map(entity, TaskResource.class);
    }
    private Task convertUpdateDtoToEntity(UpdateTaskDto updateDto){
        return mapper.map(updateDto, Task.class);
    }
    private Task convertCreateDtoToEntity(CreateTaskDto createDto){
        return mapper.map(createDto, Task.class);
    }

}