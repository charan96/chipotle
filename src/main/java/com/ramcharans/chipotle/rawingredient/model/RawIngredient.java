package com.ramcharans.chipotle.rawingredient.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "raw_ingredient")
public class RawIngredient implements Serializable {
    @Id
    private String id;
    private String name;
    
    private Integer stock;
    
    public RawIngredient(String name, Integer stock) {
        this.name = name;
        this.stock = stock;
    }
}
