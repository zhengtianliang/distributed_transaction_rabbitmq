package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ZhengTianLiang
 * @date 2020/9/12  17:10
 * @desc 系统A的消息表
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MoneypayMessage {

    // 消息ID
    private String messageId;

    // 用户ID
    private String userId;

    // 状态
    private Integer amount;

    // 修改时间
    private LocalDateTime modifyTime;

}
