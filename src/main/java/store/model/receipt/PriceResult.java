package store.model.receipt;

public class PriceResult {

    private final int total;
    private final int presentation;
    private final int membership;

    public PriceResult(int total, int presentation, int membership) {
        this.total = total;
        this.presentation = presentation;
        this.membership = membership;
    }

    public int getTotal() {
        return total;
    }

    public int finalPayment() {
        return total - (presentation + membership);
    }

    public int getPresentation() {
        return presentation;
    }

    public int getMembership() {
        return membership;
    }
}
