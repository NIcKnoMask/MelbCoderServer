package com.unimelbCoder.melbcode.Service.Rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface RabbitmqService {

    boolean enabled();

    void publishMsg(String exchange, BuiltinExchangeType exchangeType, String routingKey, String message)
            throws IOException, TimeoutException;

    void consumeMsg(String exchange, String queue, String routingKey)
            throws IOException, TimeoutException;

    void processConsumerMsg();

}
