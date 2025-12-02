package com.onlineorder.orderservice.service;


import com.onlineorder.common.config.RabbitMQConfig;
import com.onlineorder.common.events.NotificationEvent;
import com.onlineorder.common.events.OrderEvent;
import com.onlineorder.common.events.PaymentEvent;
import com.onlineorder.common.messaging.MessagePublisher;
import com.onlineorder.orderservice.client.UserClient;
import com.onlineorder.orderservice.mapper.OrderMapper;
import com.onlineorder.orderservice.model.Order;
import com.onlineorder.orderservice.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventManager {
    private final MessagePublisher messagePublisher;
    private final UserClient userClient;
    private final OrderMapper mapper;

    public void publishOrderCreated(Order order){
        OrderEvent event=mapper.toEvent(order);
        messagePublisher.publishMessage(RabbitMQConfig.ORDER_EXCHANGE,RabbitMQConfig.ORDER_CREATED_KEY,event);

        publishNotification(order,"ORDER_CREATED");
        publishPaymentProcess(order);
    }

    public void publishOrderUpdated(Order order, OrderStatus previousStatus){
        OrderEvent event=mapper.toEvent(order);
        messagePublisher.publishMessage(RabbitMQConfig.ORDER_EXCHANGE,RabbitMQConfig.ORDER_UPDATED_KEY,event);

        publishNotification(order, "ORDER_STATUS_CHANGED");
    }

    public void publishOrderCancelled(Order order,String reason){
        OrderEvent event=mapper.toEvent(order);
        messagePublisher.publishMessage(RabbitMQConfig.ORDER_EXCHANGE,RabbitMQConfig.ORDER_CANCELLED_KEY,event);

        publishNotification(order, "ORDER_CANCELLED");
    }

    //Helper
    private void publishPaymentProcess(Order order){
        PaymentEvent event=new PaymentEvent();
        event.setOrderId(order.getId());
        event.setUserId(order.getUserId());
        event.setAmount(order.getTotalPrice());
        event.setStatus(PaymentEvent.PaymentStatus.PENDING);
        event.setProcessedAt(LocalDateTime.now());

        messagePublisher.publishMessage(RabbitMQConfig.PAYMENT_EXCHANGE,RabbitMQConfig.PAYMENT_PROCESS_KEY,event);
    }

    private void publishNotification(Order order,String eventType){
        NotificationEvent notificationEvent=new NotificationEvent();
        notificationEvent.setUserId(order.getUserId());
        notificationEvent.setType(NotificationEvent.NotificationType.EMAIL);
        notificationEvent.setCreatedAt(LocalDateTime.now());

        try {
            var u=userClient.getById(order.getUserId());
            if(u!=null){
                notificationEvent.setEmail(u.email());
            }
        }
        catch (Exception e){
            log.warn("Could not fetch user email for notification: {}",e.getMessage());
        }
        Map<String,Object> templateData=new HashMap<>();
        templateData.put("orderId",order.getId());
        templateData.put("status",order.getStatus());
        templateData.put("totalPrice",order.getTotalPrice());

        switch (eventType){
            case "ORDER_CREATED"->{
                notificationEvent.setSubject("New order confirmation - #"+order.getId());
                notificationEvent.setMessage("New order has been placed.");
            }
            case "ORDER_STATUS_CHANGED"->{
                notificationEvent.setSubject("Order status changed - #"+order.getId());
                notificationEvent.setMessage("Your order status has changed to "+order.getStatus());
            }
            case "ORDER_CANCELLED"->{
                notificationEvent.setSubject("Order cancelled - #"+order.getId());
                notificationEvent.setMessage("Your order has been cancelled");
            }
        }
        notificationEvent.setTemplateData(templateData);
        messagePublisher.publishMessage(RabbitMQConfig.NOTIFICATION_EXCHANGE,RabbitMQConfig.NOTIFICATION_EMAIL_KEY,notificationEvent);
    }
}
