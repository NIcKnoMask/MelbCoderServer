package com.unimelbCoder.melbcode.bean;

public class NotifyMsg {

    private int id;

    /**
     * - 如文章收藏、评论、回复评论、点赞消息，这里存文章ID；
     * - 如系统通知消息时，这里存的是系统通知消息正文主键，也可以是0
     * - 如关注，这里就是0
     */
    private Integer relatedId;

    private Integer notifyUserId;

    private Integer operateUserId;

    private String msg;

    private Integer type;

    private Integer state;

    public NotifyMsg(int id, Integer relatedId, Integer notifyUserId, Integer operateUserId, String msg, Integer type, Integer state) {
        this.id = id;
        this.relatedId = relatedId;
        this.notifyUserId = notifyUserId;
        this.operateUserId = operateUserId;
        this.msg = msg;
        this.type = type;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Integer relatedId) {
        this.relatedId = relatedId;
    }

    public Integer getNotifyUserId() {
        return notifyUserId;
    }

    public void setNotifyUserId(Integer notifyUserId) {
        this.notifyUserId = notifyUserId;
    }

    public Integer getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(Integer operateUserId) {
        this.operateUserId = operateUserId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
