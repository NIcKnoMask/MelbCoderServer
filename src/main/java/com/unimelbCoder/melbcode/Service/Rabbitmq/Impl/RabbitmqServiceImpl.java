package com.unimelbCoder.melbcode.Service.Rabbitmq.Impl;

import com.rabbitmq.client.*;
import com.unimelbCoder.melbcode.Service.Rabbitmq.RabbitmqConnection;
import com.unimelbCoder.melbcode.Service.Rabbitmq.RabbitmqConnectionPool;
import com.unimelbCoder.melbcode.Service.Rabbitmq.RabbitmqService;
import com.unimelbCoder.melbcode.utils.CommonConstants;
import com.unimelbCoder.melbcode.utils.SpringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitmqServiceImpl implements RabbitmqService {

    @Override
    public boolean enabled() {
        return "true".equalsIgnoreCase(SpringUtils.getConfig("rabbitmq.switchFlag"));
    }

    @Override
    public void publishMsg(String exchange, BuiltinExchangeType exchangeType, String routingKey, String message) {

        try {
            //创建连接
            RabbitmqConnection rabbitmqConnection = RabbitmqConnectionPool.getConnection();
            Connection connection = rabbitmqConnection.getConnection();

            //创建消息通道
            Channel channel = connection.createChannel();

            //声明exchange中的消息为可持久化，不自动删除
            channel.exchangeDeclare(exchange, exchangeType, true, false, null);

            //发布消息
            channel.basicPublish(exchange, routingKey, null, message.getBytes());
            System.out.println("Publish msg: " + message);
            channel.close();
            RabbitmqConnectionPool.returnConnection(rabbitmqConnection);

        } catch (InterruptedException | IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void consumeMsg(String exchange, String queue, String routingKey) {

        try {
            //创建连接
            RabbitmqConnection rabbitmqConnection = RabbitmqConnectionPool.getConnection();
            Connection connection = rabbitmqConnection.getConnection();

            //创建消息通道
            final Channel channel = connection.createChannel();

            //声明消息队列
            channel.queueDeclare(queue, true, false, false, null);

            //绑定消息队列到交换机
            channel.queueBind(queue, exchange, routingKey);

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println("Consumer msg: " + message);

                    //存储进DB...
                    //获取Rabbitmq消息，并保存到DB
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            };
            //取消自动ACK
            channel.basicConsume(queue, false, consumer);
            RabbitmqConnectionPool.returnConnection(rabbitmqConnection);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void processConsumerMsg() {
        System.out.println("Begin to process Consumer msg");

        Integer stepTotal = 1;
        Integer step = 0;

        while (true) {
            try {
                System.out.println("Process Consumer Msg cycle. ");
                consumeMsg(CommonConstants.EXCHANGE_NAME_DIRECT,
                        CommonConstants.QUEUE_NAME_PRAISE,
                        CommonConstants.QUEUE_KEY_PRAISE);
                if (step.equals(stepTotal)) {
                    Thread.sleep(10000);
                    step = 0;
                }
            } catch (Exception e){}
        }
    }
}
