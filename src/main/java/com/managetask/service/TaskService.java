package com.managetask.service;

import com.managetask.model.ManageTask;
import com.managetask.model.Users;
import com.managetask.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {


//    @Autowired
//    BeanInspector beanInspector;

    @Autowired
    private   TaskRepository taskRepository;

    @CacheEvict(cacheNames = "users", allEntries = true)
    @Transactional
    public ManageTask saveTask(ManageTask task) {
        return taskRepository.save(task);
    }

    @Cacheable(cacheNames = "tasks")
    public List<ManageTask> getAll() throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newCachedThreadPool();

        // Define a Callable to fetch user data
        Callable<List<ManageTask>> fetchUserDataTask = () -> {
            System.out.println("Executing task to fetch task data in thread: " +
                    Thread.currentThread().getName());
            return taskRepository.findAll();
        };

        // Submit the Callable to the ExecutorService, obtaining a Future
        Future<List<ManageTask>> future = executorService.submit(fetchUserDataTask);

        // Perform other tasks while waiting for user data to be fetched asynchronously

        // Block and wait for the completion of the Callable
        List<ManageTask> taskData = future.get();

        // Print the fetched user data
        System.out.println("Fetched Task Data: " + taskData);

        // Shutdown the ExecutorService to release resources
        executorService.shutdown();

      return  taskData;
    }

//    @Cacheable("fetchSpecificTaskData")
    public Optional<ManageTask> getTask(long id) {
        return taskRepository.findById(id);
    }
}
