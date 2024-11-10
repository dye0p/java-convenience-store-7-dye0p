package store.model.receipt;

import java.util.List;

public class Receipt {

    private final List<BuyResult> buyResult;
    private final List<PresentationResult> presentationResult;
    private final PriceResult priceResult;

    public Receipt(List<BuyResult> buyResult, List<PresentationResult> presentationResult, PriceResult priceResult) {
        this.buyResult = buyResult;
        this.presentationResult = presentationResult;
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

    public List<PresentationResult> getPresentationResult() {
        return presentationResult;
    }

    public PriceResult getPriceResult() {
        return priceResult;
    }
}
