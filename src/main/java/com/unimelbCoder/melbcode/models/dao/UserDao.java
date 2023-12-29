package com.unimelbCoder.melbcode.models.dao;

import com.unimelbCoder.melbcode.bean.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    public User getUserByMessage(@Param("username") String username, @Param("password") String password);

    public User getUserByName(@Param("username") String username);

    public void createUser(@Param("username") String username, @Param("password") String password,
                           @Param("role") String role, @Param("email") String email, @Param("age") int age);

    public User getUserByUsername(@Param("username") String username);

    public User getUserById(@Param("id") Integer id);
}
