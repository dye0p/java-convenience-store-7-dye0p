package store.view;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import store.model.Product;
import store.model.Products;

public class OutputFormatter {

    public static String formatterProducts(Products products) {
        StringJoiner sj = new StringJoiner("\n");
        Map<String, Boolean> regularStockStatus = checkRegularStock(products);

        for (Product product : products.getProducts()) {
            sj.add(formatProductDetails(product, regularStockStatus));
        }
        return sj.toString();
    }

    private static Map<String, Boolean> checkRegularStock(Products products) {
        Map<String, Boolean> stockStatus = new HashMap<>();
        for (Product product : products.getProducts()) {
            if (product.isRegular() && product.isInStock()) {
                stockStatus.put(product.getName(), true);
            }
        }
        return stockStatus;
    }

    private static String formatProductDetails(Product product, Map<String, Boolean> stockStatus) {
        String name = product.getName();
        String quantityString = "재고 없음";

        if (product.isInStock()) {
            quantityString = product.getQuantity() + "개";
        }

        String promotionString = "";
        if (product.hasPromotion()) {
            promotionString = " " + product.getPromotion();
        }

        String productDetails = String.format("- %s %,d원 %s%s", name, product.getPrice(), quantityString,
                promotionString);

        // 일반 상품이 없고 프로모션 상품만 있는 경우 '재고 없음' 추가
        if (product.hasPromotion() && Boolean.TRUE.equals(!stockStatus.getOrDefault(name, false))) {
            String noStockDetails = String.format("- %s %,d원 재고 없음", name, product.getPrice());
            return productDetails + "\n" + noStockDetails;
        }
        return productDetails;
    }
}
