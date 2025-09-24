package com.onlineorder.notificationservice.listener;

import com.onlineorder.common.config.RabbitMQConfig;
import com.onlineorder.common.events.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationEventListener {

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_EMAIL_QUEUE)
    public void handleEmail(NotificationEvent event){
        log.info("EMAIL to userId={} subject={} message={} data={}", event.getUserId(), event.getSubject(), event.getMessage(),event.getTemplateData());
        //
    }
    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_SMS_QUEUE)
    public void handleSms(NotificationEvent event){
        log.info("SMS to userId={} message={} data={}", event.getUserId(), event.getMessage(),event.getTemplateData());
    }
}
