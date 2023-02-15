package com.sender.demo;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbotLisner {
    @RabbitListener(queues = {"q.product_queue_fanout_1"})
    public void onRecive(Product product){
        System.out.println("Event hit");
    }
}
