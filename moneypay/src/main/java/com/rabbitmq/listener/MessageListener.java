package com.rabbitmq.listener;

import com.entity.AlipayMessage;
import com.entity.MessageStatus;
import com.entity.MoneypayMessage;
import com.rabbitmq.RabbitMqSender;
import com.service.OrderService;
import com.util.json.JsonUtils;
import com.util.mapper.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * @author ZhengTianLiang
 * @date 2020/9/13  11:08
 * @desc mq的监听器, 也是核心的业务代码
 */

@Slf4j
@Component
public class MessageListener {

    @Resource
    private OrderService orderService;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private RabbitMqSender rabbitMqSender;

    @Value("${moneypay.exchange}")
    private String exchange;

    @Value("${moneypay.routkey}")
    private String routkey;


    /**
     * @author ZhengTianLiang
     * @date 2020/9/13  11:40
     * @desc 监听消息发送者(支付宝系统)发送来的消息，并作出相应的业务操作
     */
    @RabbitListener(queues = "zheng.alipay.message") // 消息发送者的队列，就是支付宝发送到的队列的队列名
    public void process(String jsonMessage) {
        AlipayMessage alipayMessage = JsonUtils.jsonToBean(jsonMessage, AlipayMessage.class);
        MoneypayMessage moneypayMessage = MapperUtils.mapperBean(alipayMessage, MoneypayMessage.class);
        // 1、去余额宝自己的消息存根中查，看看能不能根据支付宝系统传过来的消息id来查到数据，能查到则说明消费过了；查不到则是未消费过
        Integer count = orderService.queryMessageCountById(alipayMessage.getMessageId());
        Boolean exec = transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus transactionStatus) {
                if (count == 0) { //此消息未消费过
                    orderService.updateAmount(moneypayMessage.getAmount(), moneypayMessage.getUserId());// 加款操作
                    orderService.insertMessage(moneypayMessage); // 往余额宝自己的消息本地存根中插入数据
                }
                return true;
            }
        });
        if (count > 0) { // 此消息已经被消费过了，则直接通过mq去通知支付宝系统，说这个消息消费过了
            log.info("此消息已经被消费过了,不做任何操作");
        }
        if (exec){ // 去mq中發消息，通知支付宝，说此消息已经被消费过了
            alipayMessage.setStatus(MessageStatus.CONFIRM);
            rabbitMqSender.sendMessage(exchange,routkey,alipayMessage);
        }

    }

}
