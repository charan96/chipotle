package com.ramcharans.chipotle.ingredient.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("Ingredient")
public class Ingredient implements Serializable {
    public static enum Type {RICE, MEAT, VEGGIE, SALSA, BEANS, ADDON}

    private Long id;
    private String name;
    private Type type;
    private Double price;

    public Ingredient(String name, Type type, Double price) {
        this.name = name;
        this.type = type;
        this.price = price;
    }
}
