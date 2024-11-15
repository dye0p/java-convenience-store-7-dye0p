package store.model;

public class Cart {

    private final String name;
    private int quantity;

    public Cart(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void plusGiftQuantity() {
        this.quantity += 1;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
