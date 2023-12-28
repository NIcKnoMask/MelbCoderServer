package com.unimelbCoder.melbcode.Service.Rank.listener;


import com.unimelbCoder.melbcode.Service.Rank.Impl.UserRankServiceImpl;
import com.unimelbCoder.melbcode.Service.Rank.UserRankService;
import com.unimelbCoder.melbcode.Service.Rank.model.ActivityRankBo;
import com.unimelbCoder.melbcode.Service.User.Impl.UserServiceImpl;
import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.utils.NotifyMsgEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserActivityListener {

    private UserRankServiceImpl userActivityRankService = new UserRankServiceImpl();

    @EventListener(classes = NotifyMsgEvent.class)
    @Async
    public void notifyMsgListener(NotifyMsgEvent msgEvent) {
        System.out.println("successfully get the event");
        System.out.println(msgEvent.getNotifyType());
        switch (msgEvent.getNotifyType()) {
            case COMMENT:
            case REPLY:
//                CommentDO comment = (CommentDO) msgEvent.getContent();
//                userActivityRankService.addActivityScore(ReqInfoContext.getReqInfo().getUserId(), new ActivityScoreBo().setRate(true).setArticleId(comment.getArticleId()));
                break;
            case COLLECT:
//                UserFootDO foot = (UserFootDO) msgEvent.getContent();
//                userActivityRankService.addActivityScore(ReqInfoContext.getReqInfo().getUserId(), new ActivityScoreBo().setCollect(true).setArticleId(foot.getDocumentId()));
                break;
            case CANCEL_COLLECT:
//                foot = (UserFootDO) msgEvent.getContent();
//                userActivityRankService.addActivityScore(ReqInfoContext.getReqInfo().getUserId(), new ActivityScoreBo().setCollect(false).setArticleId(foot.getDocumentId()));
                break;
//            case PRAISE:
//                foot = (UserFootDO) msgEvent.getContent();
//                userActivityRankService.addActivityScore(ReqInfoContext.getReqInfo().getUserId(), new ActivityScoreBo().setPraise(true).setArticleId(foot.getDocumentId()));
//                break;
//            case CANCEL_PRAISE:
//                foot = (UserFootDO) msgEvent.getContent();
//                userActivityRankService.addActivityScore(ReqInfoContext.getReqInfo().getUserId(), new ActivityScoreBo().setPraise(false).setArticleId(foot.getDocumentId()));
//                break;
//            case FOLLOW:
//                UserRelationDO relation = (UserRelationDO) msgEvent.getContent();
//                userActivityRankService.addActivityScore(ReqInfoContext.getReqInfo().getUserId(), new ActivityScoreBo().setFollow(true).setArticleId(relation.getUserId()));
//                break;
//            case CANCEL_FOLLOW:
//                relation = (UserRelationDO) msgEvent.getContent();
//                userActivityRankService.addActivityScore(ReqInfoContext.getReqInfo().getUserId(), new ActivityScoreBo().setFollow(false).setArticleId(relation.getUserId()));
//                break;
            case CREATE_ARTICLE:
                System.out.println(msgEvent.getContent().toString());
                userActivityRankService.addActivityScore(msgEvent.getContent().toString(),
                        new ActivityRankBo().setPublishArticle(true));
            case LOGIN:
                System.out.println(msgEvent.getContent().toString());
                userActivityRankService.addActivityScore(msgEvent.getContent().toString(),
                        new ActivityRankBo().setLogin(true));
                break;
            default:
        }
    }

//    @Async
//    @EventListener(ArticleMsgEvent.class)
//    public void publishArticleListener(ArticleMsgEvent<ArticleDO> event) {
//        ArticleEventEnum type = event.getType();
//        if (type == ArticleEventEnum.ONLINE) {
//            userActivityRankService.addActivityScore(ReqInfoContext.getReqInfo().getUserId(), new ActivityScoreBo().setPublishArticle(true).setArticleId(event.getContent().getId()));
//        }
//    }
}
