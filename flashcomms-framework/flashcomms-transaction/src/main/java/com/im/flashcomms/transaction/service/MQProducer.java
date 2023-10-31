package com.im.flashcomms.transaction.service;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.im.flashcomms.transaction.annotation.SecureInvoke;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;


/**
 * Description: 发送mq工具类
 * Author: hyj
 * Date: 2023-08-12
 */
public class MQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMsg(String exchange,String key, Object body) {
        String s = JSONUtil.toJsonStr(body);
        rabbitTemplate.convertAndSend(exchange,key,s);
    }



    /**
     * 发送可靠消息，在事务提交后保证发送成功
     *
     * @param exchange
     * @param body
     */
    @SecureInvoke
    public void sendSecureMsg(String exchange, String key,Object body) {
        System.out.println("sss");
        rabbitTemplate.convertAndSend(exchange,key,body);
    }
}
