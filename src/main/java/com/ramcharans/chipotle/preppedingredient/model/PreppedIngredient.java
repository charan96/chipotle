package com.ramcharans.chipotle.preppedingredient.model;

import com.ramcharans.chipotle.rawingredient.model.RawIngredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "prepped_ingredient")
public class PreppedIngredient implements Serializable {
    public static enum Type {RICE, MEAT, VEGGIE, SALSA, BEANS, ADDON}

    @Id
    private String id;
    private String name;
    private Type type;
    private Double price;

    @DBRef
    private List<RawIngredient> rawIngredientsNeeded;

    private Integer stock;

    public PreppedIngredient(String name, Type type, Double price, List<RawIngredient> rawIngredients, Integer stock) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.rawIngredientsNeeded = rawIngredients;
        this.stock = stock;
    }

    public PreppedIngredient(String name, Type type, Double price, List<RawIngredient> rawIngredients) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.rawIngredientsNeeded = rawIngredients;
    }
}
