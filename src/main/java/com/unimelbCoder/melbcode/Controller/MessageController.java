package com.unimelbCoder.melbcode.Controller;


import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.unimelbCoder.melbcode.cache.RedisClient;
import com.unimelbCoder.melbcode.models.dto.MessageItemDTO;
import com.unimelbCoder.melbcode.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/wsMessaging")
public class MessageController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SimpUserRegistry simpUserRegistry;


    /**
     * 给指定用户发送WebSocket消息
     */
    @PostMapping("/sendToUser")
    @ResponseBody
    public String chat(@RequestBody MessageItemDTO request) {
        //消息接收者
        String receiver = request.getReceiver();
        //消息内容
        String msg = request.getContent();
        sendToUser(request.getSender(), receiver, "/topic/reply", msg);


        return "ok";
    }

    private void sendToUser(String sender, String receiver, String destination, String payload){
        Boolean isReceiverOnline = RedisClient.isUserLogin(sender);

        if(isReceiverOnline){
            messagingTemplate.convertAndSendToUser(receiver, destination, payload);
        }
        //否则将消息存储到redis，等用户上线后主动拉取未读消息
        else{
            //存储消息到Redis中
            String key = "MSG_" + receiver;
            String message = DateTime.now() + "&" + payload;
            RedisClient.addMessage(key, message);
        }
    }

    /**
     * 对应用户登录时查找并获取所有未读消息
     */
    @PostMapping("/getUnreadMsg")
    public String pullUnreadMessage(@RequestBody String username){
        HashMap<String, Object> res = new HashMap<>();
        String flag = "error";
        List<String> msg = RedisClient.getMessages(username);
        res.put("flag", flag);

        //删除redis中的缓存
        if(msg != null && msg.size() > 0){
            RedisClient.delByKey("MSG_" + username);
            res.put("data", msg); //在返回体中添加结果
        }

        return JSON.toJSONString(res);
    }

}
