package store.model;

import java.util.List;

public class Products {

    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public int getPriceByName(String name) {
        int price = 0;
        for (Product product : products) {
            //프로덕트중 해당 상품에 대한 가격을 가져온다.
            if (product.isSameName(name)) {
                price = product.getPrice();
            }
        }
        return price;
    }
}
