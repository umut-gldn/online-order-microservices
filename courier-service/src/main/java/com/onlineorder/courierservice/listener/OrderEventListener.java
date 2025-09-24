package com.onlineorder.courierservice.listener;

import com.onlineorder.common.config.RabbitMQConfig;
import com.onlineorder.common.events.OrderEvent;
import com.onlineorder.courierservice.model.Delivery;
import com.onlineorder.courierservice.model.DeliveryStatus;
import com.onlineorder.courierservice.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final DeliveryRepository repository;

    @RabbitListener(queues = RabbitMQConfig.ORDER_UPDATED_QUEUE)
    public void handleORderUpdated(OrderEvent event){
        if(event.getStatus()== OrderEvent.OrderStatus.CONFIRMED){
            Delivery delivery= Delivery.builder()
                    .orderId(event.getOrderId())
                    .status(DeliveryStatus.ASSIGNED)
                    .build();
            repository.save(delivery);
            log.info("Delivery created for order id={} status={}",event.getOrderId(),DeliveryStatus.ASSIGNED);
        }
    }
}
