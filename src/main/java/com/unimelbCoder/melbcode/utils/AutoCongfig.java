package com.unimelbCoder.melbcode.utils;

import com.unimelbCoder.melbcode.Service.Rabbitmq.RabbitmqConnectionPool;
import com.unimelbCoder.melbcode.Service.Rabbitmq.RabbitmqService;
import com.unimelbCoder.melbcode.cache.RedisClient;
import com.unimelbCoder.melbcode.models.dto.RabbitmqPropertiesDTO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
@ComponentScan(basePackages = "com.unimelbCoder.melbcode.Service")
@EnableConfigurationProperties(RabbitmqPropertiesDTO.class)
public class AutoCongfig implements ApplicationRunner {
    public AutoCongfig(RedisTemplate<String, String> redisTemplate){
        RedisClient.register(redisTemplate);
    }

    @Resource
    private RabbitmqService rabbitmqService;

    @Autowired
    private RabbitmqPropertiesDTO rabbitmqPropertiesDTO;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String host = rabbitmqPropertiesDTO.getHost();
        Integer port = rabbitmqPropertiesDTO.getPort();
        String userName = rabbitmqPropertiesDTO.getUsername();
        String password = rabbitmqPropertiesDTO.getPassword();
        String vhost = rabbitmqPropertiesDTO.getVirtualhost();
        Integer poolSize = rabbitmqPropertiesDTO.getPoolSize();
        RabbitmqConnectionPool.initRabbitmqConnectionPool(host, port, userName, password, vhost, poolSize);
        AsyncUtil.execute(() -> rabbitmqService.processConsumerMsg());
    }
}
