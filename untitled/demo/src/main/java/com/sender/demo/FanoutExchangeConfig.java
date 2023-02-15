package com.sender.demo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
class FanoutExchangeConfig {


    public static final String FANOUT_EXCHANGE = "i.fanout";
    public static final String FANOUT_QUEUE_1 = "q.product_queue_fanout_1";

    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Queue fanoutQueue1() {
        return new Queue(FANOUT_QUEUE_1);
    }

    @Bean
    public Binding binding1(FanoutExchange fanout,
                            Queue fanoutQueue1) {
        return BindingBuilder.bind(fanoutQueue1).to(fanout);
    }


}
