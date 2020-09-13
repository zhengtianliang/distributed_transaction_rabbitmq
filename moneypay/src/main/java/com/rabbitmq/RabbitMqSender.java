package com.rabbitmq;

import com.entity.MoneypayMessage;
import com.util.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ZhengTianLiang
 * @date 2020/9/12  22:29
 * @desc 消息发送者
 */

@Slf4j
@Component
public class RabbitMqSender {

    @Resource
    private AmqpTemplate amqpTemplate;

    /**
     * @param exchange   交换机名称
     * @param routingKey 路由键名称
     * @param message    要发送的消息
     * @author ZhengTianLiang
     * @date 2020/9/12  22:29
     * @desc 从支付宝系统发送消息到mq中
     */
    public void sendMessage(String exchange, String routingKey, Object message) {
        log.info("支付宝系统往mq中发送了消息：" + message);
        amqpTemplate.convertAndSend(exchange, routingKey, JsonUtils.toJson(message));
    }
}
