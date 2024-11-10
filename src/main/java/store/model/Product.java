package store.model;

public class Product {

    private final String name;
    private final int price;
    private int quantity;
    private final String promotion;

    public Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public int calculateNotPromotionQuantity(int quantity) {
        int nonPromotionQuantity = 0;

        if (promotion.equals("탄산2+1")) {
            int promotionCount = (this.quantity / 3) * 3;
            nonPromotionQuantity += quantity - promotionCount;
        }
        if (promotion.equals("MD추천상품") || promotion.equals("반짝할인")) {
            int promotionCount = (this.quantity / 2) * 2;
            nonPromotionQuantity += quantity - promotionCount;
        }
        return nonPromotionQuantity;
    }

    public void deductQuantity(int quantity) {
        this.quantity -= quantity;
    }

    public boolean isSameName(String name) {
        return this.name.equals(name);
    }

    public boolean isRegular() {
        return promotion == null;
    }

    public boolean isInStock() {
        return quantity > 0;
    }

    public boolean hasPromotion() {
        return promotion != null;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotion() {
        return promotion;
    }

}
