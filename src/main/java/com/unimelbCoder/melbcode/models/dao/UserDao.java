package com.unimelbCoder.melbcode.models.dao;

import com.unimelbCoder.melbcode.bean.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    public User getUserByMessage(@Param("username") String username, @Param("password") String password);

    public User getUserByName(@Param("id") String id);

    public void createUser(@Param("id") String id, @Param("username") String username, @Param("password") String password,
                           @Param("email") String email, @Param("role") String role, @Param("age") int age,
                           @Param("introduction") String introduction);

    public int countByUsername(@Param("username") String username);
}
