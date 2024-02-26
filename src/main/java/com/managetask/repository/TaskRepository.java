package com.managetask.repository;

import com.managetask.model.ManageTask;
import com.managetask.model.Users;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
//@Cacheable("taskCache")
public interface TaskRepository extends JpaRepository<ManageTask,Long> {


}
