package com.unimelbCoder.melbcode.config;

import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.cache.RedisClient;
import com.unimelbCoder.melbcode.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class websocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    JwtUtils jwtUtils;


    @Bean
    public DefaultHandshakeHandler defaultHandshakeHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                String token = request.getHeaders().getFirst("Authorization");
                User userInfo = jwtUtils.getUserInfoFromToken(token, User.class);
                System.out.println(userInfo.toString());
                String username = userInfo.getUsername();
                return () -> username;
            }
        };
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-websocket")
                .setHandshakeHandler(defaultHandshakeHandler())
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //客户端需要把消息发送到/message/xxx地址
        registry.setApplicationDestinationPrefixes("/message");

        //服务端广播消息的路径前缀，客户端需要相应订阅/topic/yyy这个地址的消息
        registry.enableSimpleBroker("/topic");
    }

}
