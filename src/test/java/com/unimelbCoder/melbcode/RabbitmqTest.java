package com.unimelbCoder.melbcode;

import com.rabbitmq.client.BuiltinExchangeType;
import com.unimelbCoder.melbcode.Service.Rabbitmq.RabbitmqService;
import com.unimelbCoder.melbcode.utils.CommonConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitmqTest {

    @Autowired
    RabbitmqService rabbitmqService;

    @Test
    void testProductMq() {
        try {
            rabbitmqService.publishMsg(
                    CommonConstants.EXCHANGE_NAME_DIRECT,
                    BuiltinExchangeType.DIRECT,
                    CommonConstants.QUEUE_KEY_PRAISE,
                    "rabbitmq test code1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testConsumeMq() {
        try {
            rabbitmqService.consumeMsg(
                    CommonConstants.EXCHANGE_NAME_DIRECT,
                    CommonConstants.QUEUE_NAME_PRAISE,
                    CommonConstants.QUEUE_KEY_PRAISE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
