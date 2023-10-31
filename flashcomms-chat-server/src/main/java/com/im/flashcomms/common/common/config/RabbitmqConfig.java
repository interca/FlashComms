package com.im.flashcomms.common.common.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbit的配置类  绑定队列和交换机
 * @author hyj
 * @since  2022-12-15
 */
@Configuration
public class RabbitmqConfig {

    /**
     * 交换机
     */
    public  static  final String FLASHCOMMS_MSG_EXCHANGE = "flascomms-msg-exchange";


    /**
     * 消息发送mq
     */
     public static  final String  SEND_MSG_QUEUE = "chat_send_msg_queue";
     public static final String SEND_MSG_KEY = "chat_send_msg_key";

    /**
     * push用户
     */
    public static final String PUSH_QUEUE = "websocket_push_queue";
    public static final String PUSH_KEY = "websocket_push_key";

    /**
     * (授权完成后)登录信息mq
     */
    public static final String LOGIN_MSG_QUEUE = "user_login_send_msg_queue";
    public static final  String LOGIN_MSG_KEY = "user_login_send_msg_key";



    /**
     * 扫码成功 信息发送mq
     */
    public static final String SCAN_MSG_QUEUE = "user_scan_send_msg_queue";
    public static final String SCAN_MSG_KEY = "user_scan_send_msg_key";



    /**
     * 交换机
     * @return
     */
    @Bean(name = FLASHCOMMS_MSG_EXCHANGE)
    public Exchange bootExchange1(){
        return ExchangeBuilder.topicExchange(FLASHCOMMS_MSG_EXCHANGE).durable(true).build();
    }


    /**
     * 消息发送
     */
    @Bean(name = SEND_MSG_QUEUE)
    public Queue bootQueue1(){
      return QueueBuilder.durable(SEND_MSG_QUEUE).build();
    }

    /**
     * push用户
     */
    @Bean(name = PUSH_QUEUE)
    public Queue bootQueue2(){
        return QueueBuilder.durable(PUSH_QUEUE).build();
    }

    /**
     * (授权完成后)登录信息mq
     */
    @Bean(name = LOGIN_MSG_QUEUE)
    public Queue bootQueue3(){
        return QueueBuilder.durable(LOGIN_MSG_QUEUE).build();
    }


    /**
     * 扫码成功
     */
    @Bean(name = SCAN_MSG_QUEUE)
    public Queue bootQueue4(){
        return QueueBuilder.durable(SCAN_MSG_QUEUE).build();
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindQueueExchange1(
            @Qualifier(FLASHCOMMS_MSG_EXCHANGE) Exchange exchange,
            @Qualifier(SEND_MSG_QUEUE) Queue queue){
       return BindingBuilder.bind(queue).to(exchange).with(SEND_MSG_KEY).noargs();
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindQueueExchange2(
            @Qualifier(FLASHCOMMS_MSG_EXCHANGE) Exchange exchange,
            @Qualifier(PUSH_QUEUE) Queue queue ){
        return BindingBuilder.bind(queue).to(exchange).with(PUSH_KEY).noargs();
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindQueueExchange3(
            @Qualifier(FLASHCOMMS_MSG_EXCHANGE) Exchange exchange,
            @Qualifier(LOGIN_MSG_QUEUE) Queue queue ){
        return BindingBuilder.bind(queue).to(exchange).with(LOGIN_MSG_KEY).noargs();
    }


    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindQueueExchange4(
            @Qualifier(FLASHCOMMS_MSG_EXCHANGE) Exchange exchange,
            @Qualifier(SCAN_MSG_QUEUE) Queue queue ){
        return BindingBuilder.bind(queue).to(exchange).with(SCAN_MSG_KEY).noargs();
    }


}
