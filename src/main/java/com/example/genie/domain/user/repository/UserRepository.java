package com.example.genie.domain.user.repository;

import com.example.genie.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUserLoginId(String userLoginId);

}
