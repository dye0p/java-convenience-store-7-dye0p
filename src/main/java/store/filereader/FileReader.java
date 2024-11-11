package store.filereader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import store.model.Product;
import store.model.Products;
import store.model.Promotion;
import store.model.Promotions;

public class FileReader {

    private static final String PRODUCTS_FILE_PATH = "src/main/resources/products.md";
    private static final String PROMOTIONS_FILE_PATH = "src/main/resources/promotions.md";
    private static final String CATEGORY_DELIMITER = ",";
    private static final String NULL_PROMOTION = "null";
    private static final String FILE_READER_ERROR = "파일을 읽는 중 오류가 발생했습니다: ";

    public static Products readerProductFile() {
        List<Product> products = new ArrayList<>();

        File file = new File(PRODUCTS_FILE_PATH);
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(file))) {
            String line;
            skipFirstLine(br);
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(CATEGORY_DELIMITER);
                mappingProducts(fields, products);
            }
        } catch (IOException e) {
            System.out.println(FILE_READER_ERROR + e.getMessage());
        }
        return new Products(products);
    }

    public static Promotions readerPromotionFile() {
        List<Promotion> promotions = new ArrayList<>();

        File file = new File(PROMOTIONS_FILE_PATH);
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(file))) {
            String line;
            skipFirstLine(br);
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(CATEGORY_DELIMITER);
                mappingPromotions(fields, promotions);
            }
        } catch (IOException e) {
            System.out.println(FILE_READER_ERROR + e.getMessage());
        }
        return new Promotions(promotions);
    }

    private static void mappingProducts(String[] fields, List<Product> products) {
        String name = fields[0];
        int price = Integer.parseInt(fields[1]);
        int quantity = Integer.parseInt(fields[2]);
        String promotion = fields[3];
        if (isNullPromotion(promotion)) {
            promotion = null;
        }
        products.add(new Product(name, price, quantity, promotion));
    }

    private static void mappingPromotions(String[] fields, List<Promotion> promotions) {
        String name = fields[0];
        int buy = Integer.parseInt(fields[1]);
        int get = Integer.parseInt(fields[2]);
        LocalDateTime startDate = LocalDate.parse(fields[3]).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(fields[4]).atStartOfDay();

        promotions.add(new Promotion(name, buy, get, startDate, endDate));
    }

    private static boolean isNullPromotion(String promotion) {
        return promotion.equals(NULL_PROMOTION);
    }

    private static void skipFirstLine(BufferedReader br) throws IOException {
        br.readLine();
    }
}
