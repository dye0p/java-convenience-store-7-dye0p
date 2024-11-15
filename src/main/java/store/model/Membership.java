package store.model;

public class Membership {

    private static final double DISCOUNT_PERCENTAGE = 0.3;
    private static final int MAX_DISCOUNT_PRICE = 8000;

    private int discountPrice = 0;

    public int calculateDiscount(int nonPromotionPrice) {
        discountPrice += nonPromotionPrice * DISCOUNT_PERCENTAGE;
        return Math.min(discountPrice, MAX_DISCOUNT_PRICE);
    }
}
