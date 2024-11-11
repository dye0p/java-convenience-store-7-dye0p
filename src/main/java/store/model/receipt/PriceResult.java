package store.model.receipt;

public class PriceResult {

    private final int total;
    private final int gift;
    private final int membership;

    public PriceResult(int total, int gift, int membership) {
        this.total = total;
        this.gift = gift;
        this.membership = membership;
    }

    public int finalPayment() {
        return total - (gift + membership);
    }

    public int getTotal() {
        return total;
    }

    public int getGift() {
        return gift;
    }

    public int getMembership() {
        return membership;
    }
}
