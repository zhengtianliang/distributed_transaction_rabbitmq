package com.alipay;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author ZhengTianLiang
 * @date 2020/9/12  16:42
 * @desc 系统A(支付宝系统)的启动类
 */

@SpringBootApplication
@EnableScheduling
@EnableRabbit
public class AlipayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlipayApplication.class, args);
    }

}
