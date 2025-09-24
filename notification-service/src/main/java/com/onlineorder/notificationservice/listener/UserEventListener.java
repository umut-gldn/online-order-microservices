package com.onlineorder.notificationservice.listener;

import com.onlineorder.common.config.RabbitMQConfig;
import com.onlineorder.common.events.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserEventListener {

    @RabbitListener(queues = RabbitMQConfig.USER_CREATED_QUEUE)
    public void handleUserCreated(UserEvent event){
        log.info("User created with id={} email={}",event.getUserId(), event.getEmail());
    }
}
