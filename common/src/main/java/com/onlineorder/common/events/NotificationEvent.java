package com.onlineorder.common.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {
    private Long userId;
    private String email;
    private String phoneNumber;
    private NotificationType type;
    private String subject;
    private String message;
    private Map<String, Object> templateData;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    public enum NotificationType {
        EMAIL, SMS, PUSH, IN_APP
    }
}