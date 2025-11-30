package com.baap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.baap.model.User;



public interface UserRepository extends JpaRepository<User, Long> {

   public User findByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u WHERE u.fullName LIKE %:query% OR u.email LIKE %:query%")
  public  List<User> searchUser(@Param("query") String query);
    
    @Query("SELECT u FROM User u JOIN u.followers f WHERE f.id = :userId")
    List<User> findFollowersByUserId(@Param("userId") Long userId);

    @Query("SELECT u FROM User u JOIN u.following f WHERE f.id = :userId")
    List<User> findFollowingByUserId(@Param("userId") Long userId);
    
 // Fetch all users (members)
    @Query("SELECT u FROM User u")
    List<User> getAllMembers();
}
