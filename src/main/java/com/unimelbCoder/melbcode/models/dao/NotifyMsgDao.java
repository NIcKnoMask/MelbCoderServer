package com.unimelbCoder.melbcode.models.dao;

import com.unimelbCoder.melbcode.bean.NotifyMsg;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyMsgDao {

    /**
     * 查询关联用户消息记录
     * @param notifyUserId
     * @param operateUserId
     * @param type
     * @param relatedId
     * @return
     */
    public NotifyMsg getNotifyMsgByRelatedIdAndType(@Param("notify_user_id") Integer notifyUserId,
                                                    @Param("operate_user_id") Integer operateUserId,
                                                    @Param("type") Integer type,
                                                    @Param("related_id") Integer relatedId);

    public NotifyMsg getNotifyMsgByIdx(@Param("notify_user_id") Integer notifyUserId,
                                       @Param("type") Integer type,
                                       @Param("state") Integer state);

    public Integer countByUserIdAndStat(@Param("notify_user_id") Integer notifyUserId,
                                    @Param("state") Integer state);

    public void createNotifyMsg(@Param("related_id") Integer relatedId,
                                @Param("notify_user_id") Integer notifyUserId,
                                @Param("operate_user_id") Integer operateUserId,
                                @Param("msg") String msg,
                                @Param("type") Integer type,
                                @Param("state") Integer state);


}
