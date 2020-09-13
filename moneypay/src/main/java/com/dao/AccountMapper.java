package com.dao;

import com.entity.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

/**
 * @author ZhengTianLiang
 * @date 2020/9/12  16:42
 * @desc 系统A(支付宝系统)的账户表的mapper
 */

public interface AccountMapper {

    /**
     * @author ZhengTianLiang
     * @date 2020/9/13  10:50
     * @desc 账户表的加款操作
     */
    @Update(" update account set amount = amount + #{amount}, modifyTime = now() where user_id = #{id} ")
    int updateAmountById(Account account);

    /**
     * @author ZhengTianLiang
     * @date 2020/9/13  10:47
     * @desc 账户表的新增操作
     */
    @Insert(" insert into account(user_id, amount, modify_time) values(#{userId}, #{amount}, now() ) ")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertAccount(Account account);
}
