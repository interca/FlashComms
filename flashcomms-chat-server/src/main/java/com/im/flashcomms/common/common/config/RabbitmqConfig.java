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
     * 消息交换机
     */
    public  static  final String FLASHCOMMS_MSG_EXCHANGE = "flascomms-msg-exchange";


    /**
     * 消息的队列
     */
    public  static  final String FLASHCOMMS_MSG_QUEUE = "flascomms-msg-queue";




    /**
     * 抢票交换机
     * @return
     */
    @Bean(name = "flascomms-msg-exchange")
    public Exchange bootExchange1(){
        return ExchangeBuilder.topicExchange(FLASHCOMMS_MSG_EXCHANGE).durable(true).build();
    }


    /**
     * 抢票队列
     */
    @Bean(name = "flascomms-msg-queue")
    public Queue bootQueue1(){
      return QueueBuilder.durable(FLASHCOMMS_MSG_QUEUE).build();
    }


    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindQueueExchange1(@Qualifier("flascomms-msg-queue") Queue queue , @Qualifier("flascomms-msg-exchange") Exchange exchange){
       return BindingBuilder.bind(queue).to(exchange).with("flashcomms").noargs();
    }



}
