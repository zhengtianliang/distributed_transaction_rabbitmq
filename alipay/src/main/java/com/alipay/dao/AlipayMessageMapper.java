package com.alipay.dao;

import com.alipay.entity.AlipayMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author ZhengTianLiang
 * @date 2020/9/12  21:36
 * @desc 系统A(支付宝系统)的消息存根表的mapper
 */

public interface AlipayMessageMapper {

    /**
     * @author ZhengTianLiang
     * @date 2020/7/3  21:43
     * @desc 往消息存根表中插入一条数据
     */
    @Insert(" insert into alipay_message(message_id, user_id, modifyTime, status) " +
            " values(#{messageId}, #{userId}, now(), #{status} ) ")
    int insertMessage(AlipayMessage message);

    /**
     * @author ZhengTianLiang
     * @date 2020/7/3  21:30
     * @desc 将消息的状态改为“已确认”
     */
    @Update(" update alipay_message set status = #{status}, modifyTime = now() where message_id = #{messageId} ")
    int updateMessageSatus(AlipayMessage message);

    /**
     * @author ZhengTianLiang
     * @date 2020/7/3  21:44
     * @desc 查出全部的状态是“未确认”的消息
     */
    @Select(" select message_id, user_id, modifyTime, status from alipay_message where status = #{statue} ")
    List<AlipayMessage> queryNoConfirmMessageList(@Param("status") String status);

}
