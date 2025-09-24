package com.onlineorder.orderservice.listener;

import com.onlineorder.common.config.RabbitMQConfig;
import com.onlineorder.common.events.PaymentEvent;
import com.onlineorder.orderservice.model.OrderStatus;
import com.onlineorder.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {

    private final OrderService orderService;

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_COMPLETED_QUEUE)
    public void handlePaymentCompleted(PaymentEvent paymentEvent){
        if(paymentEvent.getStatus()== PaymentEvent.PaymentStatus.SUCCESS){
            orderService.updateOrderStatus(paymentEvent.getOrderId(), OrderStatus.CONFIRMED);
        }
        else {
            orderService.cancelOrder(paymentEvent.getOrderId(), "Payment failed");
        }
    }
}
