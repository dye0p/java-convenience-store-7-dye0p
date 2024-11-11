package store.model.receipt;

import java.util.List;

public class Receipt {

    private final List<BuyResult> buyResult;
    private final List<GiftResult> giftResult;
    private final PriceResult priceResult;

    public Receipt(List<BuyResult> buyResult, List<GiftResult> giftResult, PriceResult priceResult) {
        this.buyResult = buyResult;
        this.giftResult = giftResult;
        this.priceResult = priceResult;
    }

    public int totalBuyQuantity() {
        int sum = 0;
        for (BuyResult result : buyResult) {
            sum += result.getQuantity();
        }
        return sum;
    }

    public List<BuyResult> getBuyResult() {
        return buyResult;
    }

    public List<GiftResult> getPresentationResult() {
        return giftResult;
    }

    public PriceResult getPriceResult() {
        return priceResult;
    }
}
