package ru.yandex.model;

import java.util.List;

public class CreateOrderRequest {

    private List<String> ingredients;

    public CreateOrderRequest(OrderData orderData) {
        this(orderData.getIngredients());
    }

    public CreateOrderRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
