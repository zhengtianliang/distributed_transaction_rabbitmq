package com.util.timer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ZhengTianLiang
 * @date 2020/9/12  21:49
 * @desc 定时器，去定时扫描消息存根表，拿到状态是未确认的消息，再次发送到MQ中
 */

@Component
@Slf4j
public class MessageSchdule {
}
