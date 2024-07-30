package com.unimelbCoder.melbcode.models.dao;

import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.unimelbCoder.melbcode.bean.Location;
import java.util.List;

@Repository
public interface LocationsDao {
    public Location getLocationById(@Param("id") String id);

    public List<Location> getLocationsByIds(@Param("ids") List<Integer> ids);
}
