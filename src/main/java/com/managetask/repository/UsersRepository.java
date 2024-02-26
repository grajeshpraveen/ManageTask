package com.managetask.repository;

import com.managetask.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersRepository extends JpaRepository<Users, String> {

    List<Users> findByUsername(String username);


}
