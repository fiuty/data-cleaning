package com.example.multidatasource.domain.repository;

import com.example.multidatasource.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
