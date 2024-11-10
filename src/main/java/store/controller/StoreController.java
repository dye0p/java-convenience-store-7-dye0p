package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.exception.ErrorMessage;
import store.filereader.FileReader;
import store.model.Cart;
import store.model.EventResult;
import store.model.Product;
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

        EventResult event = event(carts);
    }

    private EventResult event(List<Cart> carts) {
        Map<String, Integer> promotionEventResult = new HashMap<>();
        Map<Cart, Integer> nonPromotionResult = new HashMap<>();
        int giftCount = 0;
        for (Cart cart : carts) {
            giftCount = progressEvent(cart, giftCount, promotionEventResult, nonPromotionResult);
        }

        return new EventResult(promotionEventResult, giftCount, nonPromotionResult);
    }

    private int progressEvent(Cart cart, int giftCount, Map<String, Integer> promotionEventMap,
                              Map<Cart, Integer> nonPromotionItems) {
        if (productManager.isPromotionProduct(cart.getName())) {
            Product promotionProduct = productManager.getPromotionByName(cart.getName());
            if (promotionProduct != null) {
                giftCount = todayPromotionAvailable(cart, promotionProduct, giftCount, promotionEventMap);
            }
        }
        return giftCount;
    }

    private int todayPromotionAvailable(Cart cart, Product promotionProduct, int giftCount,
                                        Map<String, Integer> promotionEventMap) {
        LocalDateTime now = DateTimes.now();
        if (promotionManager.isTodayPromotionAvailable(promotionProduct.getPromotion(), now)) {

            giftCount = progressPromotionEvent(cart, giftCount, promotionProduct, promotionEventMap);
        }
        return giftCount;
    }

    private int progressPromotionEvent(Cart cart, int giftCount, Product promotionProduct,
                                       Map<String, Integer> promotionEventMap) {
        giftCount = notEnoughPromotionQuantity(cart, promotionProduct, giftCount, promotionEventMap);
        return giftCount;
    }

    private int notEnoughPromotionQuantity(Cart cart, Product promotionProduct, int giftCount,
                                           Map<String, Integer> promotionEventMap) {
        if (cart.getQuantity() > promotionProduct.getQuantity()) {
            int nonPromotionQuantity = promotionManager.notPromotionByQuantity(cart.getQuantity(), promotionProduct);
            giftCount = selectPromotionQuantity(cart, nonPromotionQuantity, promotionProduct, promotionEventMap,
                    giftCount);
        }
        return giftCount;
    }

    private int selectPromotionQuantity(Cart cart, int nonPromotionQuantity, Product promotionProduct,
                                        Map<String, Integer> promotionEventMap, int giftCount) {
        String choice = inputView.readEnoughPromotionQuantity(cart.getName(), nonPromotionQuantity);
        if (choice.equals("Y")) {
            giftCount = giftAccept(cart, promotionProduct, promotionEventMap, giftCount);
        }
        if (choice.equals("N")) {
            giftRefuse(cart, promotionProduct);
        }

        return giftCount;
    }

    private int giftAccept(Cart cart, Product promotionProduct, Map<String, Integer> promotionEventMap, int giftCount) {
        //받을 수 있는 증정 개수 담기
        int maxGiftCount = promotionManager.calculateGiftCount(promotionProduct);
        promotionEventMap.put(cart.getName(), maxGiftCount);
        giftCount += promotionProduct.getPrice() * maxGiftCount;
        //일부 수량에 대해 정가로 결제한다.
        //프로모션 상품은 모두 차감하고 모자란 만킄 일반 재고에서 나머지를 차감한다.
        //일반 재고에서 나머지를 차감한다.
        int beforeQuantity = promotionProduct.getQuantity();
        promotionProduct.deductQuantity(beforeQuantity);
        Product basicByName = productManager.getBasicByName(cart.getName());
        int quantity = cart.getQuantity() - beforeQuantity;
        basicByName.deductQuantity(quantity);
        return giftCount;
    }

    private void giftRefuse(Cart cart, Product promotionProduct) {
        // 정가로 결제해야하는 수량만큼 제외한 후 결제를 진행한다.
        int quantity = cart.getQuantity() - promotionProduct.getQuantity();
        cart.changeQuantity(quantity); //구입 수량 변경
        promotionProduct.deductQuantity(promotionProduct.getQuantity());
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
            productManager.validateExistent(cart.getName());
        }
    }

    private void validateExceedQuantity(List<Cart> carts) {
        for (Cart cart : carts) {
            int quantity = cart.getQuantity();
            int productQuantity = productManager.sumProductQuantityByName(cart.getName());
            if (quantity > productQuantity) {
                throw new IllegalArgumentException(ErrorMessage.EXCEED_STOCK_QUANTITY.getMessage());
            }
        }
    }
}
