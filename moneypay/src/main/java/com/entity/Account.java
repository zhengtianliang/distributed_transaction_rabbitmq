package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ZhengTianLiang
 * @date 2020/9/12  17:13
 * @desc 账户表
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    // 用户id
    private String userId;

    // 扣款的价格
    private Integer amount;

    // 修改时间
    private LocalDateTime modifyTime;

}
