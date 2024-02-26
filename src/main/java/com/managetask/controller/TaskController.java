package com.managetask.controller;

import com.managetask.model.ManageTask;
import com.managetask.model.Users;
import com.managetask.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger logger = LogManager.getLogger(TaskController.class);


    @Autowired
    TaskService taskService;


    @Operation(summary = "Insert data ")
    @PostMapping("/insert")

    public ResponseEntity<ManageTask> insertData(@RequestBody ManageTask task) throws Exception
    {
        logger.info("insert method started");
        ManageTask createdTask = new ManageTask();
        try {
            createdTask =  taskService.saveTask(task);
        }catch (Exception e)
        {
            System.out.println(e.getLocalizedMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }
    @GetMapping("/getbyId/{taskId}")
    public ResponseEntity<?> getData(@PathVariable int taskId) throws Exception
    {
        return new ResponseEntity<>(taskService.getTask(taskId), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllData() throws Exception
    {
        return new ResponseEntity<>(taskService.getAll(), HttpStatus.OK);
    }

}
