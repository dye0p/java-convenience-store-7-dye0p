package store.filereader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import store.model.Product;
import store.model.Products;

public class FileWriter {

    private static final String PRODUCTS_FILE_PATH = "src/main/resources/products.md";
    private static final String NULL_PROMOTION = "null";

    private final Products products;

    public FileWriter(Products products) {
        this.products = products;
    }

    public void updateProducts() {
        List<Product> productList = products.getProducts();

        File file = new File(PRODUCTS_FILE_PATH);
        try (BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(file, false))) {
            bw.write("name,price,quantity,promotion\n");

            for (Product product : productList) {
                bw.write(formatProduct(product));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("파일 쓰기 오류: " + e.getMessage());
        }
    }

    private String formatProduct(Product product) {
        String promotion = product.getPromotion();

        if (product.getPromotion() == null) {
            promotion = NULL_PROMOTION;
        }
        return product.getName() + "," + product.getPrice() + "," + product.getQuantity() + "," + promotion;
    }
}
