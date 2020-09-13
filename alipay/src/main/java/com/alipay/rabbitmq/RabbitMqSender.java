package com.alipay.rabbitmq;

import com.alipay.entity.AlipayMessage;
import com.alipay.util.json.JsonUtils;
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
     * @author ZhengTianLiang
     * @date 2020/9/12  22:29
     * @desc 从支付宝系统发送消息到mq中
     * @param exchange 交换机名称
     * @param routingKey 路由键名称
     * @param message 要发送的消息
     */
    public void sendMessage(String exchange, String routingKey, AlipayMessage message) {
        log.info("支付宝系统往mq中发送了消息：" + message.getMessageId());
        amqpTemplate.convertAndSend(exchange, routingKey, JsonUtils.toJson(message));
    }
}
