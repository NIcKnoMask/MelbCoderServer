package com.unimelbCoder.melbcode.Service.Rank;

import com.unimelbCoder.melbcode.Service.Rank.model.ActivityRankBo;
import com.unimelbCoder.melbcode.models.dto.RankItemDTO;
import com.unimelbCoder.melbcode.models.enums.ActivityRankTimeEnum;

import java.util.List;

public interface UserRankService {
    /**
     * 添加活跃分
     *
     * @param username
     * @param activityScore
     */
    void addActivityScore(String username, ActivityRankBo activityScore);

//    /**
//     * 查询用户的活跃信息
//     *
//     * @param username
//     * @param time
//     * @return
//     */
//    RankItemDTO queryRankInfo(String username, ActivityRankTimeEnum time);

    /**
     * 查询活跃度排行榜
     *
     * @param time
     * @return
     */
    List<RankItemDTO> queryRankList(ActivityRankTimeEnum time, int size);
}
