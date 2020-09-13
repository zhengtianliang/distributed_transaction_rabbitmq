package com.dao;

import com.entity.MoneypayMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author ZhengTianLiang
 * @date 2020/9/12  21:36
 * @desc 系统A(支付宝系统)的消息存根表的mapper
 */

public interface MoneypayMessageMapper {

    /**
     * @author ZhengTianLiang
     * @date 2020/9/13  10:46
     * @desc 往消息存根表中插入一条数据
     */
    @Insert(" insert into moneypay_message(message_id, user_id, amount, modifyTime) " +
            " values(#{messageId}, #{userId}, #{amount}, now() ) ")
    int insertMessage(MoneypayMessage message);

    /**
     * @author ZhengTianLiang
     * @date 2020/9/13  10:47
     * @desc 根据消息id，查出消息的数量(若数据大于0，则说明已经消费过此消息了)
     */
    @Select(" select count(*) from moneypay_message where message_id = #{messageId} ")
    int queryCountByMessageId(@Param("messageId") String messageId);


}
