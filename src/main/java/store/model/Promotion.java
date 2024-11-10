package store.model;

import java.time.LocalDateTime;

public class Promotion {

    private final String name;
    private final int buy;
    private final int get;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public Promotion(String name, int buy, int get, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isPossiblePromotionQuantity(int quantity, Product promotionProduct) {
        //구입 수량이 혜택을 받을수 있는 수량인지 체크
        if (promotionProduct.getPromotion().equals("탄산2+1")) {
            return quantity % 3 == 0;
        }

        if (promotionProduct.getPromotion().equals("MD추천상품") || promotionProduct.getPromotion().equals("반짝할인")) {
            return quantity % 2 == 0;
        }
        return false;
    }

    public boolean isWithinPromotionDate(LocalDateTime now) {
        return (now.isEqual(startDate) || now.isAfter(startDate)) && (now.isEqual(endDate) || now.isBefore(endDate));
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }
}
