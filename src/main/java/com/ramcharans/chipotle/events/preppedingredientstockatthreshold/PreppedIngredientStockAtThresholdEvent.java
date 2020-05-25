package com.ramcharans.chipotle.events.preppedingredientstockatthreshold;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ramcharans.chipotle.preppedingredient.model.PreppedIngredient;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PreppedIngredientStockAtThresholdEvent {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime timestamp;
    private PreppedIngredient preppedIngredient;
    
    public PreppedIngredientStockAtThresholdEvent(PreppedIngredient preppedIngredient) {
        timestamp = LocalDateTime.now();
        this.preppedIngredient = preppedIngredient;
    }
}
