package com.alipay.entity;

/**
 * @author ZhengTianLiang
 * @date 2020/9/12  17:15
 * @desc 消息的状态
 */

public enum MessageStatus {

    CONFIRM("已确认"),
    NOCONFIRM("未确认");

    private String status;

    MessageStatus(String statue){
        this.status = statue;
    }

    public String getStatus(){
        return status;
    }
}
