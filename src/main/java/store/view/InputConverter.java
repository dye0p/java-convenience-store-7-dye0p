package store.view;

import java.util.ArrayList;
import java.util.List;
import store.exception.ErrorMessage;
import store.exception.InputValidation;
import store.model.Cart;

public class InputConverter {

    private static final int PRODUCT_NAME_INDEX = 0;
    private static final int PRODUCT_QUANTITY_INDEX = 1;
    private static final String PRODUCT_DELIMITER = ",";

    public static List<Cart> converter(String input) {
        InputValidation.validateNullOrBlank(input);

        String[] products = input.split(PRODUCT_DELIMITER);

        List<Cart> carts = new ArrayList<>();
        for (String product : products) {
            Cart cart = createCart(product);
            carts.add(cart);
        }

        return carts;
    }

    private static Cart createCart(String product) {
        InputValidation.validateFormat(product);
        String[] productAndQuantity = formatProduct(product);

        String name = productAndQuantity[PRODUCT_NAME_INDEX];
        int quantity = parseQuantity(productAndQuantity);

        return new Cart(name, quantity);
    }

    private static String[] formatProduct(String product) {
        String replaceProduct = product.replaceAll("[\\[\\]]", "");
        return separateProductAndQuantity(replaceProduct);
    }

    private static int parseQuantity(String[] productAndQuantity) {
        try {
            return Integer.parseInt(productAndQuantity[PRODUCT_QUANTITY_INDEX]);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT_FORMAT.getMessage());
        }
    }

    private static String[] separateProductAndQuantity(String input) {
        String[] productAndQuantity = input.split("-");
        InputValidation.validateSize(productAndQuantity);
        return productAndQuantity;
    }
}
