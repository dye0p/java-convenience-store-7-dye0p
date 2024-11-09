package store.model;

import java.util.HashMap;
import java.util.Map;

public class ProductManager {

    private Map<String, Product> basicProducts = new HashMap<>();
    private Map<String, Product> promotionProducts = new HashMap<>();

    public ProductManager(Products products) {
        for (Product product : products.getProducts()) {
            if (product.getPromotion() != null) {
                promotionProducts.put(product.getName(), product);
            } else {
                basicProducts.put(product.getName(), product);
            }
        }
    }
}
