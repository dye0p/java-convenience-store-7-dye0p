package store.model;

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
}