package com.ramcharans.chipotle.events.preppedingredientreplenished;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ramcharans.chipotle.preppedingredient.model.PreppedIngredient;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PreppedIngredientReplenishedEvent {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime timestamp;
    private PreppedIngredient preppedIngredient;
    
    public PreppedIngredientReplenishedEvent(PreppedIngredient preppedIngredient) {
        timestamp = LocalDateTime.now();
        this.preppedIngredient = preppedIngredient;
    }
}
