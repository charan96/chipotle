package com.ramcharans.chipotle.ingredient.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ingredient")
public class Ingredient implements Serializable {
    public static enum Type {RICE, MEAT, VEGGIE, SALSA, BEANS, ADDON}

    @Id
    private String id;
    private String name;
    private Type type;
    private Double price;

    public Ingredient(String name, Type type, Double price) {
        this.name = name;
        this.type = type;
        this.price = price;
    }
}
