package com.unimelbCoder.melbcode.models.dao;

import com.unimelbCoder.melbcode.bean.Article;
import org.redisson.misc.Hash;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.unimelbCoder.melbcode.bean.Location;
import java.util.*;

@Repository
public interface UserStarDao {

    // 添加收藏
    public void addStar(@Param("user_id") String user_id, @Param("object_id") int object_id, @Param("date") String date,
                        @Param("type") String type);

    public void deleteStar(@Param("user_id") String user_id, @Param("object_id") int object_id, @Param("date") String date,
                        @Param("type") String type);

    // 根据日期 获取收藏的位置的列表
    public List<Map<String, Object>> getStarLocations(@Param("user_id") String user_id);

    // 获取收藏的文章的列表
    public List<Article> getStarArticle(@Param("user_id") String user_id);

    // 判断是否已收藏
    public boolean hadStar(@Param("user_id") String user_id, @Param("object_id") int object_id);

    public List<Location> getStarLocationByDate(@Param("user_id") String user_id, @Param("date") String date);
}
