package com.alipay.rabbitmq.listener;

import com.alipay.entity.AlipayMessage;
import com.alipay.service.OrderService;
import com.alipay.util.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ZhengTianLiang
 * @date 2020/9/13  12:08
 * @desc 支付宝系统的mq的监听器,
 */

@Slf4j
@Component
public class MessageListener {

    @Resource
    private OrderService orderService;

    /**
     * @author ZhengTianLiang
     * @date 2020/9/13  12:09
     * @desc 监听余额宝发送的消息，用来改变支付宝自己的消息存根表的消息状态
     */
    @RabbitListener(queues = "zheng.moneypay.message") // 它监听的是余额宝的消息队列
    public void process(String message){
        orderService.updateMessageStatus(message); // 更新消息的状态
    }


}
