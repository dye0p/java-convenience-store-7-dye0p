package store.filereader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.model.Product;
import store.model.Products;

public class FileReader {

    public static Products readerFile() {
        List<Product> products = new ArrayList<>();

        File file = new File("src/main/resources/products.md");

        try (BufferedReader br = new BufferedReader(new java.io.FileReader(file))) {
            String line;

            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                String name = fields[0];
                int price = Integer.parseInt(fields[1]);
                int quantity = Integer.parseInt(fields[2]);

                String promotion = fields[3];
                if (fields[3].equals("null")) {
                    promotion = null;
                }

                products.add(new Product(name, price, quantity, promotion));
            }
        } catch (IOException e) {
            System.out.println("파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
        }
        return new Products(products);
    }
}
