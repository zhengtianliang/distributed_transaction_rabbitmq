package com.service.impl;

import com.dao.AccountMapper;
import com.dao.MoneypayMessageMapper;
import com.entity.Account;
import com.entity.MoneypayMessage;
import com.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author ZhengTianLiang
 * @date 2020/9/13  11:27
 * @desc service
 */

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    MoneypayMessageMapper messageMapper;

    @Resource
    AccountMapper accountMapper;


    /**
     * @author ZhengTianLiang
     * @date 2020/9/13  11:31
     * @desc 这个是账户的新增操作
     */
    @Override
    public void updateAmount(Integer amount, String userId) {
        Account account = new Account(userId,amount, LocalDateTime.now());
        accountMapper.updateAmountById(account);
    }

    /**
     * @author ZhengTianLiang
     * @date 2020/9/13  11:27
     * @desc 根据消息id查到消息的数量(用来判断该消息是否被消费过)
     */
    @Override
    public Integer queryMessageCountById(String messageId) {
        return messageMapper.queryCountByMessageId(messageId);
    }

    /**
     * @author ZhengTianLiang
     * @date 2020/9/13  11:27
     * @desc 往余额宝系统的消息存根表中插入数据
     */
    @Override
    public void insertMessage(MoneypayMessage message) {
        messageMapper.insertMessage(message);
    }
}
