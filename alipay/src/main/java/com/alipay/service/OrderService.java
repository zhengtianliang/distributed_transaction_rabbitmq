package com.alipay.service;


/**
 * @author ZhengTianLiang
 * @date 2020/9/12  21:57
 * @desc service
 */

public interface OrderService {


    /**
     * @author ZhengTianLiang
     * @date 2020/9/12  21:57
     * @desc 更新余额操作         系统1(支付宝系统的)的扣款操作
     */
    void updateAmount(int amount,String userId);


    /**
     * @author ZhengTianLiang
     * @date 2020/9/12  22:54
     * @desc 支付宝监听到了余额宝发来的消息以后，修改消息存根表的状态，改为“已确认”
     */
    void updateMessageStatus(String message);
}
