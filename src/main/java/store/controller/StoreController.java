package store.controller;

import java.util.List;
import store.exception.ErrorMessage;
import store.filereader.FileReader;
import store.model.Cart;
import store.model.ProductManager;
import store.model.Products;
import store.model.PromotionManager;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private final InputView inputView;
    private final OutputView outputView;
    private ProductManager productManager;
    private PromotionManager promotionManager;

    public StoreController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.promotionManager = new PromotionManager(FileReader.readerPromotionFile().getPromotions());
    }

    public void run() {
        Products products = readeProducts();
        displayProducts(products);

        List<Cart> carts = tryReadItem();
    }

    private Products readeProducts() {
        Products products = FileReader.readerProductFile();
        productManager = new ProductManager(products);
        return products;
    }

    private void displayProducts(Products products) {
        outputView.printWellComeMessage();
        outputView.printProducts(products);
    }

    private List<Cart> tryReadItem() {
        while (true) {
            try {
                List<Cart> carts = inputView.readItem();
                validate(carts);
                return carts;
            } catch (IllegalArgumentException exception) {
                outputView.printErrorMessage(exception.getMessage());
            }
        }
    }

    private void validate(List<Cart> carts) {
        validateNonexistent(carts);
        validateExceedQuantity(carts);
    }

    private void validateNonexistent(List<Cart> carts) {
        for (Cart cart : carts) {
            productManager.containsProduct(cart.getName());
        }
    }

    private void validateExceedQuantity(List<Cart> carts) {
        for (Cart cart : carts) {
            int quantity = cart.getQuantity();
            int productQuantity = productManager.getProductQuantityByName(cart.getName());
            if (quantity > productQuantity) {
                throw new IllegalArgumentException(ErrorMessage.EXCEED_STOCK_QUANTITY.getMessage());
            }
        }
    }
}
