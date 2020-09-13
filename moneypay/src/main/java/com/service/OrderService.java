package com.service;

import com.entity.MoneypayMessage;

/**
 * @author ZhengTianLiang
 * @date 2020/9/13  11:09
 * @desc service
 */

public interface OrderService {

    void updateAmount(Integer amount, String userId);

    Integer queryMessageCountById(String messageId);

    void insertMessage(MoneypayMessage message);
}
