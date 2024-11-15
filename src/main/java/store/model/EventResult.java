package store.model;

import java.util.Map;

public class EventResult {

    private final Map<String, Integer> promotionEventResult;
    private final int giftCount;
    private final Map<Cart, Integer> nonPromotionEventResult;

    public EventResult(Map<String, Integer> promotionEventResult, int giftCount,
                       Map<Cart, Integer> nonPromotionEventResult) {
        this.promotionEventResult = promotionEventResult;
        this.giftCount = giftCount;
        this.nonPromotionEventResult = nonPromotionEventResult;
    }

    public Map<String, Integer> getPromotionEventResult() {
        return promotionEventResult;
    }

    public int getGiftCount() {
        return giftCount;
    }

    public Map<Cart, Integer> getNonPromotionEventResult() {
        return nonPromotionEventResult;
    }

}
