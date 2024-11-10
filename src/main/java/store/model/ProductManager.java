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

    public Product getPromotionByName(String name) {
        Product product = promotionProducts.get(name);
        if (product.getPromotion() != null) {
            return product;
        }
        return null;
    }

    public boolean isPromotionProduct(String productName) {
        Product promotionProduct = this.promotionProducts.get(productName);
        if (promotionProduct == null) {
            return false;
        }
        return promotionProduct.getPromotion() != null;
    }


    public int sumProductQuantityByName(String name) {
        int sum = 0;
        return sumQuantity(name, sum);
    }

    public Product getBasicByName(String name) {
        return basicProducts.get(name);
    }

    private int sumQuantity(String name, int sum) {
        sum = sumBasicProductQuantity(name, sum);
        sum = sumPromotionProductQuantity(name, sum);
        return sum;
    }

    private int sumBasicProductQuantity(String name, int sum) {
        for (Product product : basicProducts.values()) {
            if (product.isSameName(name)) {
                sum += product.getQuantity();
            }
        }
        return sum;
    }

    private int sumPromotionProductQuantity(String name, int sum) {
        for (Product product : promotionProducts.values()) {
            if (product.isSameName(name)) {
                sum += product.getQuantity();
            }
        }
        return sum;
    }

    public void validateExistent(String name) {
        if (!basicProducts.containsKey(name) && !promotionProducts.containsKey(name)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_FOUNT_PRODUCT.getMessage());
        }
    }
}
