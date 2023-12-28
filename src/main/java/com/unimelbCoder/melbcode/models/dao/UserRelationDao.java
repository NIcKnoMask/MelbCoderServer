package com.unimelbCoder.melbcode.models.dao;

import com.unimelbCoder.melbcode.models.dto.UserRelationDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRelationDao {
    public void followUser(@Param("mainUser") String mainUser, @Param("followUsername") String followUsername);

    public void unFollowUser(@Param("mainUser") String mainUser, @Param("followUsername") String followUsername);

    public void updateFollow(@Param("mainUser") String mainUser, @Param("followUsername") String followUsername);

    public boolean hasUser(@Param("mainUser") String mainUser, @Param("followUsername") String followUser);

    public List<UserRelationDTO> listUserFollows(@Param("mainUser") String mainUser);

    public List<UserRelationDTO> queryUserFans(@Param("followUser") String followUser);
}
