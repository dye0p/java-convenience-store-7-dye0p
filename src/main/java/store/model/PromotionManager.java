package store.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromotionManager {

    private Map<String, Promotion> promotionMap = new HashMap<>();

    public PromotionManager(List<Promotion> promotions) {
        for (Promotion promotion : promotions) {
            promotionMap.put(promotion.getName(), promotion);
        }
    }

    public boolean isTodayPromotionAvailable(String promotionName, LocalDateTime now) {
        Promotion promotion = promotionMap.get(promotionName);
        return promotion != null && promotion.isWithinPromotionDate(now);
    }

    public int notPromotionByQuantity(int quantity, Product promotionProduct) {
        return promotionProduct.calculateNotPromotionQuantity(quantity);
    }

    public int calculateGiftCount(Product promotionProduct) {
        //남은 수량에서 최대 몇개의 증정을 받을 수 있는지 찾는다.
        int quantity = promotionProduct.getQuantity();
        Promotion promotion = promotionMap.get(promotionProduct.getPromotion());
        if (promotion == null) {
            return 0;
        }
        int buy = promotion.getBuy();
        int get = promotion.getGet();

        return quantity / (buy + get);
    }
}
