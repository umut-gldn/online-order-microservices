package com.onlineorder.common.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {
    // Exchange Names
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String PAYMENT_EXCHANGE = "payment.exchange";
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    public static final String USER_EXCHANGE = "user.exchange";

    // Queue Names
    public static final String ORDER_CREATED_QUEUE = "order.created.queue";
    public static final String ORDER_UPDATED_QUEUE = "order.updated.queue";
    public static final String ORDER_CANCELLED_QUEUE = "order.cancelled.queue";
    public static final String PAYMENT_PROCESS_QUEUE = "payment.process.queue";
    public static final String PAYMENT_COMPLETED_QUEUE = "payment.completed.queue";
    public static final String NOTIFICATION_EMAIL_QUEUE = "notification.email.queue";
    public static final String NOTIFICATION_SMS_QUEUE = "notification.sms.queue";
    public static final String USER_CREATED_QUEUE = "user.created.queue";

    // Routing Keys
    public static final String ORDER_CREATED_KEY = "order.created";
    public static final String ORDER_UPDATED_KEY = "order.updated";
    public static final String ORDER_CANCELLED_KEY = "order.cancelled";
    public static final String PAYMENT_PROCESS_KEY = "payment.process";
    public static final String PAYMENT_SUCCESS_KEY = "payment.success";
    public static final String PAYMENT_FAILED_KEY = "payment.failed";
    public static final String NOTIFICATION_EMAIL_KEY = "notification.email";
    public static final String NOTIFICATION_SMS_KEY = "notification.sms";
    public static final String USER_CREATED_KEY = "user.created";

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate=new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
    // Order Exchange & Queues
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue orderCreatedQueue() {
        return QueueBuilder.durable(ORDER_CREATED_QUEUE).build();
    }

    @Bean
    public Queue orderUpdatedQueue() {
        return QueueBuilder.durable(ORDER_UPDATED_QUEUE).build();
    }

    @Bean
    public Queue orderCancelledQueue() {
        return QueueBuilder.durable(ORDER_CANCELLED_QUEUE).build();
    }
    @Bean
    public Binding orderCreatedBinding() {
        return BindingBuilder
                .bind(orderCreatedQueue())
                .to(orderExchange())
                .with(ORDER_CREATED_KEY);
    }

    @Bean
    public Binding orderUpdatedBinding() {
        return BindingBuilder
                .bind(orderUpdatedQueue())
                .to(orderExchange())
                .with(ORDER_UPDATED_KEY);
    }

    @Bean
    public Binding orderCancelledBinding(){
        return BindingBuilder
                .bind(orderCancelledQueue())
                .to(orderExchange())
                .with(ORDER_CANCELLED_KEY);
    }
    // Payment Exchange & Queues
    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public Queue paymentProcessQueue() {
        return QueueBuilder.durable(PAYMENT_PROCESS_QUEUE).build();
    }

    @Bean
    public Queue paymentCompletedQueue() {
        return QueueBuilder.durable(PAYMENT_COMPLETED_QUEUE).build();
    }

    @Bean
    public Binding paymentProcessBinding() {
        return BindingBuilder
                .bind(paymentProcessQueue())
                .to(paymentExchange())
                .with(PAYMENT_PROCESS_KEY);
    }

    @Bean
    public Binding paymentCompletedBinding() {
        return BindingBuilder
                .bind(paymentCompletedQueue())
                .to(paymentExchange())
                .with(PAYMENT_SUCCESS_KEY);
    }
    @Bean
    public Binding paymentFailedBinding(){
        return BindingBuilder
                .bind(paymentCompletedQueue())
                .to(paymentExchange())
                .with(PAYMENT_FAILED_KEY);
    }

    // Notification Exchange & Queues
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Queue notificationEmailQueue() {
        return QueueBuilder.durable(NOTIFICATION_EMAIL_QUEUE).build();
    }

    @Bean
    public Queue notificationSmsQueue() {
        return QueueBuilder.durable(NOTIFICATION_SMS_QUEUE).build();
    }

    @Bean
    public Binding notificationEmailBinding() {
        return BindingBuilder
                .bind(notificationEmailQueue())
                .to(notificationExchange())
                .with(NOTIFICATION_EMAIL_KEY);
    }

    @Bean
    public Binding notificationSmsBinding() {
        return BindingBuilder
                .bind(notificationSmsQueue())
                .to(notificationExchange())
                .with(NOTIFICATION_SMS_KEY);
    }

    // User Exchange & Queues
    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }

    @Bean
    public Queue userCreatedQueue() {
        return QueueBuilder.durable(USER_CREATED_QUEUE).build();
    }

    @Bean
    public Binding userCreatedBinding() {
        return BindingBuilder
                .bind(userCreatedQueue())
                .to(userExchange())
                .with(USER_CREATED_KEY);
    }

}
