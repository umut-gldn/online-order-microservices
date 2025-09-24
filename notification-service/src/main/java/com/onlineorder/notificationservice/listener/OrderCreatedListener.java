package com.onlineorder.notificationservice.listener;


import com.onlineorder.common.config.RabbitMQConfig;
import com.onlineorder.common.events.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderCreatedListener {
    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
    public void handleOrderCreated(OrderEvent event){
        log.info("Order created with id={} userId={}",event.getOrderId(), event.getUserId());
    }
}
