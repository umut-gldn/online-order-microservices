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
        log.info("Email sending initiated...To {}",event.getEmail());
        try {
        Thread.sleep(500);
            log.info("Email sent  Subject: {} Message: {}",event.getSubject(), event.getMessage());
        }
        catch (InterruptedException e){
            log.error("Error while sending email: {}",e.getMessage());
        }
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_SMS_QUEUE)
    public void handleSms(NotificationEvent event){
        log.info("Sms sending started... Tel {}",event.getPhoneNumber());
        try {
            Thread.sleep(200);
            log.info("Sms sent {}",event.getMessage());

        } catch (InterruptedException e) {
            log.error("Sms delivery interrupted: {}",e.getMessage());
        }
    }

}
