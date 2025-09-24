package com.onlineorder.paymentservice.listener;


import com.onlineorder.common.config.RabbitMQConfig;
import com.onlineorder.common.events.PaymentEvent;
import com.onlineorder.common.messaging.MessagePublisher;
import com.onlineorder.paymentservice.model.Payment;
import com.onlineorder.paymentservice.model.PaymentStatus;
import com.onlineorder.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentProcessListener {
    private final PaymentRepository repository;
    private final MessagePublisher messagePublisher;

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_PROCESS_QUEUE)
    public void handlePaymentProcess(PaymentEvent request){
        log.info("Processing payment for order id={} userId={} amount={}", request.getOrderId(), request.getUserId(), request.getAmount());

        Payment payment=Payment.builder()
                .orderId(request.getOrderId())
                .userId(request.getUserId())
                .amount(request.getAmount())
                .status(PaymentStatus.PENDING)
                .build();
        payment=repository.save(payment);

        boolean approved=simulateThirdPartyPayment(request.getAmount());
        payment.setStatus(approved ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        payment=repository.save(payment);

        PaymentEvent event=new PaymentEvent();
        event.setOrderId(payment.getOrderId());
        event.setPaymentId(payment.getId());
        event.setUserId(payment.getUserId());
        event.setAmount(payment.getAmount());
        event.setPaymentMethod("CARD");
        event.setStatus(approved ? PaymentEvent.PaymentStatus.SUCCESS : PaymentEvent.PaymentStatus.FAILED);
        event.setTransactionId(UUID.randomUUID().toString());

        if(!approved){
            event.setFailureReason("Insufficient funds");
        }
        event.setProcessedAt(LocalDateTime.now());

        String routingKey=approved ? RabbitMQConfig.PAYMENT_SUCCESS_KEY : RabbitMQConfig.PAYMENT_FAILED_KEY;
        messagePublisher.publishMessage(RabbitMQConfig.PAYMENT_EXCHANGE,routingKey,event);
    }
    private boolean simulateThirdPartyPayment(BigDecimal amount){
        return amount==null || amount.compareTo(BigDecimal.valueOf(1000))<0;
    }
}
