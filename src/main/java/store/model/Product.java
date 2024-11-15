package store.model;

public class Product {

    private static final int ONE_PLUS_OEN_CONDITION = 2;
    private static final int TWO_PLUS_ONE_CONDITION = 3;

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

    public int calculatePromotionGift(Cart cart) {
        int giftCount = 0;
        giftCount = twoPlusOnePromotionCalculate(cart, giftCount);
        giftCount = onePlusOenPromotionCalculate(cart, giftCount);
        return giftCount;
    }

    public int calculateNotPromotionQuantity(int quantity) {
        int nonPromotionQuantity = 0;

        if (promotion.equals("탄산2+1")) {
            int promotionCount = (this.quantity / TWO_PLUS_ONE_CONDITION) * TWO_PLUS_ONE_CONDITION;
            nonPromotionQuantity += quantity - promotionCount;
        }
        if (promotion.equals("MD추천상품") || promotion.equals("반짝할인")) {
            int promotionCount = (this.quantity / ONE_PLUS_OEN_CONDITION) * ONE_PLUS_OEN_CONDITION;
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

    private int onePlusOenPromotionCalculate(Cart cart, int giftCount) {
        if (this.promotion.equals("MD추천상품") || this.promotion.equals("반짝할인")) {
            int cartQuantity = cart.getQuantity();
            giftCount += cartQuantity / ONE_PLUS_OEN_CONDITION;
        }
        return giftCount;
    }

    private int twoPlusOnePromotionCalculate(Cart cart, int giftCount) {
        if (this.promotion.equals("탄산2+1")) {
            int cartQuantity = cart.getQuantity();
            giftCount += cartQuantity / TWO_PLUS_ONE_CONDITION;
        }
        return giftCount;
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
