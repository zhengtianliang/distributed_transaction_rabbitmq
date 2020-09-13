package com.alipay.util.timer;

import com.alipay.dao.AlipayMessageMapper;
import com.alipay.entity.AlipayMessage;
import com.alipay.entity.MessageStatus;
import com.alipay.rabbitmq.RabbitMqSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZhengTianLiang
 * @date 2020/9/12  21:49
 * @desc 定时器，
 */

@Component
@Slf4j
public class MessageSchdule {

    @Resource
    private AlipayMessageMapper messageMapper;

    @Resource
    private RabbitMqSender rabbitMqSender;

    @Value("${alipay.exchange}")
    private String exchange;

    @Value("${alipay.routkey}")
    private String routkey;

    /**
     * @author ZhengTianLiang
     * @date 2020/9/13  12:12
     * @desc 定时器，去定时扫描消息存根表，拿到状态是未确认的消息，再次发送到MQ中
     */
    @Scheduled(cron = "0/5 0/1 * * * ?") // 每5秒执行一次
    public void sendMessage(){
        List<AlipayMessage> alipayMessages = messageMapper.queryNoConfirmMessageList(MessageStatus.NOCONFIRM.toString());
        alipayMessages.forEach(s -> {
            rabbitMqSender.sendMessage(exchange,routkey,s);
        });
    }


}
