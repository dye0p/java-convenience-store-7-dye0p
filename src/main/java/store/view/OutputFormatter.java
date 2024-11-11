package store.view;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import store.model.Product;
import store.model.Products;

public class OutputFormatter {

    private static final String NEXT_LINE = System.lineSeparator();
    private static final String PRODUCTS_DETAILS_FORMAT = "- %s %,d원 %s%s";
    private static final String OUT_OF_QUANTITY_DETAILS_FORMAT = "- %s %,d원 재고 없음";

    public static String formatterProducts(Products products) {
        StringJoiner sj = new StringJoiner(NEXT_LINE);
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
        String quantity = "재고 없음";

        if (product.isInStock()) {
            quantity = product.getQuantity() + "개";
        }

        String promotion = "";
        if (product.hasPromotion()) {
            promotion = " " + product.getPromotion();
        }

        String productDetails = String.format(PRODUCTS_DETAILS_FORMAT, name, product.getPrice(), quantity, promotion);

        if (product.hasPromotion() && Boolean.TRUE.equals(!stockStatus.getOrDefault(name, false))) {
            String noStockDetails = String.format(OUT_OF_QUANTITY_DETAILS_FORMAT, name, product.getPrice());
            return productDetails + NEXT_LINE + noStockDetails;
        }
        return productDetails;
    }
}
