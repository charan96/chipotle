package com.ramcharans.chipotle.events.mealfulfilled;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MealFulfilledEvent {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime timestamp;
    String orderId;

    public MealFulfilledEvent(String orderId) {
        timestamp = LocalDateTime.now();
        this.orderId = orderId;
    }
}
