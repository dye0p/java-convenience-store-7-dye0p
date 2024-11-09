package store.model;

import java.util.HashMap;
import java.util.Map;
import store.exception.ErrorMessage;

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

    public int getProductQuantityByName(String name) {
        int sum = 0;
        for (Product product : basicProducts.values()) {
            if (product.isSameName(name)) {
                sum += product.getQuantity();
            }
        }
        for (Product product : promotionProducts.values()) {
            if (product.isSameName(name)) {
                sum += product.getQuantity();
            }
        }
        return sum;
    }

    public void containsProduct(String name) {
        if (!basicProducts.containsKey(name) && !promotionProducts.containsKey(name)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_FOUNT_PRODUCT.getMessage());
        }
    }
}
