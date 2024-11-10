package store.model.receipt;

public class PresentationResult {

    private final String name;
    private final int quantity;

    public PresentationResult(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
