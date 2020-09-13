package com.alipay.service.impl;

import com.alipay.dao.AccountMapper;
import com.alipay.dao.AlipayMessageMapper;
import com.alipay.entity.Account;
import com.alipay.entity.AlipayMessage;
import com.alipay.entity.MessageStatus;
import com.alipay.rabbitmq.RabbitMqSender;
import com.alipay.service.OrderService;
import com.alipay.util.json.JsonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author ZhengTianLiang
 * @date 2020/9/12  22:00
 * @desc service
 */

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private AlipayMessageMapper alipayMessageMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private RabbitMqSender rabbitMqSender;

    /**
     * @author ZhengTianLiang
     * @date 2020/9/12  21:57
     * @desc 更新余额操作         系统1(支付宝系统的)的扣款操作
     */
    // @Transactional    /**
    //     * 不能使用这个注解的原因是，mq发送消息是比较占用资源的，但是这个又不是必须成功的。也就是说步骤1、2
    //     * 是必须同步的。要么同事成功，要么同事失败。而步骤3是可以失败的。因为我要是没往ma发送成功消息，无非就是消息存根表
    //     * 的状态一直是未确认，那我定时任务会再次发送的。但是步骤1和步骤2是必须要么同时成功，要么同时失败。
    //     * 所以我们要尽量避免使用声明式事务，可以使用编程式事务(因为往mq发送消息比较消耗内存，又不是事务,无须与前俩绑定的)
    //     */
    @Override
    public void updateAmount(int amount, String userId) {
        // 创建一个消息存根对象，用来插入到消息存根表、mq发送消息也用这个对象
        AlipayMessage message = new AlipayMessage(UUID.randomUUID().toString(), userId, LocalDateTime.now(), MessageStatus.NOCONFIRM);
        // 使用编程式事务
        Integer execute = transactionTemplate.execute(new TransactionCallback<Integer>() {
            @Override
            public Integer doInTransaction(TransactionStatus transactionStatus) {
                // 1、account进行扣款操作
                Account account = new Account(userId, amount, LocalDateTime.now());
                int i = accountMapper.updateAmountById(account);
                // 2、第一步执行成功的话，往消息存根表中插入消息
                if (i == 1) {
                    return alipayMessageMapper.insertMessage(message); // 消息存根表成功插入了数据
                }
                return 0; // 未成功插入数据
            }
        });
        if (execute > 0){ // 3、步骤1,2都成功的话，往mq中发送消息
            rabbitMqSender.sendMessage("","",message);
        }
    }


    /**
     * @author ZhengTianLiang
     * @date 2020/9/13  10:19
     * @desc 支付宝监听到了余额宝发来的消息以后，修改消息存根表的状态，改为“已确认”
     */
    /**
     * 有些童鞋可能要问了，这明明是一个单表操作，为啥要加上@Transactional呢？
     * 因为如果不加事务的话，整个连接池就会有两个链接，也就是说，在同一个类中，有的是用了@Transactional注解，
     * 有的类没有用@Transactional注解，那么就会导致，有两个连接池对象，一个是spring维护的，一个是mybatis维护的
     * 假设没有用@Transactional注解，又用到了mybatis的update，那么mybatis的源码就会从连接池中获取一个链接对象，而
     * 这个链接对象是由mybatis维护、管理的。
     * 而如果使用了@Transactional注解(或者编程式事务)，即使用到了mybatis的update方法，那么也是从spring中拿的连接池对象，
     * 这一切都是由spring维护、管理的。
     * 总结：下面这个方法，加不加@Transactional注解，执行的效果是一样的，但是加了的话，连接池对象会从2个变成1个，
     * 降低了整个系统的开销。本来设计的初衷就是为了提高系统的吞吐量，要是有两个连接池对象，与设计初衷不符，所以加了此注解
     */
    @Transactional
    @Override
    public void updateMessageStatus(String message) { // 余额宝传过来的消息的状态是confirm已确认的
        AlipayMessage alipayMessage = JsonUtils.jsonToBean(message, AlipayMessage.class);
        alipayMessageMapper.updateMessageSatus(alipayMessage);
    }
}
