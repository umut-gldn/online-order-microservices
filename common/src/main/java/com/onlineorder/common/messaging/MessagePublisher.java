package com.onlineorder.common.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessagePublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishMessage(String exchange, String routingKey, Object message){
        try {
            log.info("Sending message to exchange: {} with routing key: {} message:{}", exchange, routingKey,message);
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            log.info("Message sent successfully");
        }
        catch (Exception e){
            log.error("Error while sending message to exchange: {} with routing key: {}", exchange, routingKey,e);
            throw  new RuntimeException("Failed to publish message",e);
        }
    }
    public void publishMessageWithDelay(String exchange, String routingKey, Object message, Long delayMs){
        try{
            log.info("Sending message to exchange: {} with routing key: {} message:{} with delay:{}", exchange, routingKey,message,delayMs);
            rabbitTemplate.convertAndSend(exchange, routingKey, message, messageProcessor -> {
                messageProcessor.getMessageProperties().setDelayLong(delayMs);
                return messageProcessor;
            });
            log.info("Delayed Message sent successfully");
        } catch (Exception e) {
            log.error("Error while sending message to exchange: {} with routing key: {}", exchange, routingKey,e);
            throw  new RuntimeException("Failed to publish message",e);
        }
    }

}
