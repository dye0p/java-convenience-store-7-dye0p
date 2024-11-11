package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.exception.ErrorMessage;
import store.filereader.FileReader;
import store.filereader.FileWriter;
import store.model.Cart;
import store.model.EventResult;
import store.model.Membership;
import store.model.Product;
import store.model.ProductManager;
import store.model.Products;
import store.model.PromotionManager;
import store.model.receipt.BuyResult;
import store.model.receipt.PresentationResult;
import store.model.receipt.PriceResult;
import store.model.receipt.Receipt;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private static final String YES_CHOICE = "Y";
    private static final String NO_CHOICE = "N";

    private final InputView inputView;
    private final OutputView outputView;
    private ProductManager productManager;
    private final PromotionManager promotionManager;

    public StoreController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.promotionManager = new PromotionManager(FileReader.readerPromotionFile().getPromotions());
    }

    public void run() {
        Products products = readeProducts();
        displayProducts(products);

        List<Cart> carts = tryReadItem();

        EventResult promotionEventResult = event(carts);

        int membershipDiscount = membershipEvent(promotionEventResult);

        productsQuantityUpdate(products);

        displayReceipt(carts, products, promotionEventResult, membershipDiscount);

        continueRetry();
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

    private int progressEvent(Cart cart, int giftCount, Map<String, Integer> promotionEventResult,
                              Map<Cart, Integer> nonPromotionItems) {
        if (productManager.isPromotionProduct(cart.getName())) {
            Product promotionProduct = productManager.getPromotionByName(cart.getName());
            if (promotionProduct != null) {
                return todayPromotionAvailable(cart, promotionProduct, giftCount, promotionEventResult);
            }
        }
        regularProductPayment(cart, nonPromotionItems);

        return giftCount;
    }

    private void regularProductPayment(Cart cart, Map<Cart, Integer> nonPromotionItems) {
        Product basicProduct = productManager.getBasicByName(cart.getName());
        nonPromotionItems.put(cart, basicProduct.getPrice());
        basicProduct.deductQuantity(cart.getQuantity());
    }

    private int todayPromotionAvailable(Cart cart, Product promotionProduct, int giftCount,
                                        Map<String, Integer> promotionEventResult) {
        LocalDateTime now = DateTimes.now();
        if (promotionManager.isTodayPromotionAvailable(promotionProduct.getPromotion(), now)) {
            giftCount = progressPromotionEvent(cart, giftCount, promotionProduct, promotionEventResult);
        }
        return giftCount;
    }

    private int progressPromotionEvent(Cart cart, int giftCount, Product promotionProduct,
                                       Map<String, Integer> promotionEventResult) {
        if (cart.getQuantity() > promotionProduct.getQuantity()) {
            return notEnoughPromotionQuantity(cart, promotionProduct, giftCount, promotionEventResult);
        }
        if (cart.getQuantity() <= promotionProduct.getQuantity() && !promotionManager.isNotEnoughPromotionQuantity(
                cart.getQuantity(), promotionProduct)) {
            return handleAddGift(cart, promotionProduct, giftCount, promotionEventResult);
        }
        return defaultPromotion(cart, giftCount, promotionProduct, promotionEventResult);
    }

    private int notEnoughPromotionQuantity(Cart cart, Product promotionProduct, int giftCount,
                                           Map<String, Integer> promotionEventResult) {
        int nonPromotionQuantity = promotionManager.notPromotionByQuantity(cart.getQuantity(), promotionProduct);
        return selectPromotionQuantity(cart, nonPromotionQuantity, promotionProduct, promotionEventResult, giftCount);
    }

    private int selectPromotionQuantity(Cart cart, int nonPromotionQuantity, Product promotionProduct,
                                        Map<String, Integer> promotionEventResult, int giftCount) {

        String choice = tryReadEnoughPromotionQuantity(cart, nonPromotionQuantity);
        if (choice.equals(YES_CHOICE)) {
            giftCount = giftAccept(cart, promotionProduct, promotionEventResult, giftCount);
        }
        if (choice.equals(NO_CHOICE)) {
            giftRefuse(cart, promotionProduct);
        }
        return giftCount;
    }


    private int giftAccept(Cart cart, Product promotionProduct, Map<String, Integer> promotionEventResult,
                           int giftCount) {
        int maxGiftCount = promotionManager.calculateGiftCount(promotionProduct);
        giftCount = updateGiftCount(cart, promotionProduct, promotionEventResult, giftCount, maxGiftCount);

        int beforeQuantity = promotionProduct.getQuantity();
        promotionProduct.deductQuantity(beforeQuantity);

        Product basicByName = productManager.getBasicByName(cart.getName());
        deductRegularQuantity(cart, beforeQuantity, basicByName);

        return giftCount;
    }

    private int updateGiftCount(Cart cart, Product promotionProduct, Map<String, Integer> promotionEventResult,
                                int giftCount, int maxGiftCount) {
        promotionEventResult.put(cart.getName(), maxGiftCount);
        giftCount += promotionProduct.getPrice() * maxGiftCount;
        return giftCount;
    }

    private void deductRegularQuantity(Cart cart, int beforeQuantity, Product basicByName) {
        int quantity = cart.getQuantity() - beforeQuantity;
        basicByName.deductQuantity(quantity);
    }

    private void giftRefuse(Cart cart, Product promotionProduct) {
        int quantity = cart.getQuantity() - promotionProduct.getQuantity();
        cart.changeQuantity(quantity);
        promotionProduct.deductQuantity(promotionProduct.getQuantity());
    }

    private int handleAddGift(Cart cart, Product promotionProduct, int giftCount,
                              Map<String, Integer> promotionEventResult) {
        String choice = tryReadGift(cart);
        if (choice.equals(YES_CHOICE)) {
            giftCount = plusGiftCount(cart, promotionProduct, giftCount, promotionEventResult);
        }

        return giftCount;
    }

    private int plusGiftCount(Cart cart, Product promotionProduct, int giftCount,
                              Map<String, Integer> promotionEventResult) {
        int existingGiftCount = getGiftCount(cart, promotionProduct);
        giftCount += promotionProduct.getPrice();

        cart.plusGiftQuantity();
        promotionEventResult.put(cart.getName(), existingGiftCount + 1);

        promotionProduct.deductQuantity(cart.getQuantity());
        return giftCount;
    }

    private int defaultPromotion(Cart cart, int giftCount, Product promotionProduct,
                                 Map<String, Integer> promotionEventResult) {
        int freeCount = getGiftCount(cart, promotionProduct);
        promotionEventResult.put(cart.getName(), freeCount);
        giftCount += promotionProduct.getPrice() * freeCount;
        promotionProduct.deductQuantity(cart.getQuantity());
        return giftCount;
    }

    private int getGiftCount(Cart cart, Product promotionProduct) {
        return promotionProduct.calculatePromotionGift(cart);
    }

    private int membershipEvent(EventResult eventResult) {
        String choice = tryReadMembership();
        int discountPrice = 0;
        if (choice.equals(YES_CHOICE)) {
            discountPrice = progressDiscount(eventResult);
        }
        return discountPrice;
    }

    private int progressDiscount(EventResult eventResult) {
        Membership membership = new Membership();
        int nonPromotionPrice = getNonPromotionPrice(eventResult);
        return membership.calculateDiscount(nonPromotionPrice);
    }

    private int getNonPromotionPrice(EventResult eventResult) {
        Map<Cart, Integer> nonPromotionProducts = eventResult.getNonPromotionEventResult();
        return calculateNonPromotionPrice(nonPromotionProducts);
    }

    private int calculateNonPromotionPrice(Map<Cart, Integer> nonPromotionProducts) {
        int nonPromotionPrice = 0;
        for (Cart cart : nonPromotionProducts.keySet()) {
            int price = nonPromotionProducts.get(cart);
            nonPromotionPrice += cart.getQuantity() * price;
        }
        return nonPromotionPrice;
    }

    private void productsQuantityUpdate(Products products) {
        FileWriter fileWriter = new FileWriter(products);
        fileWriter.updateProducts();
    }

    private void displayReceipt(List<Cart> carts, Products products, EventResult promotionEventResult,
                                int membershipDiscount) {
        List<BuyResult> buyResults = getBuyResults(carts, products);

        List<PresentationResult> presentationResults = getPresentationResults(promotionEventResult, carts);

        int totalPrice = getTotalPrice(carts, products);

        createReceipt(totalPrice, promotionEventResult, membershipDiscount, buyResults, presentationResults);
    }

    private List<BuyResult> getBuyResults(List<Cart> carts, Products products) {
        List<BuyResult> buyResults = new ArrayList<>();
        for (Cart cart : carts) {
            String name = cart.getName();
            int quantity = cart.getQuantity();
            int price = products.getPriceByName(cart.getName());
            BuyResult buyResult = new BuyResult(name, quantity, price);
            buyResults.add(buyResult);
        }
        return buyResults;
    }

    private List<PresentationResult> getPresentationResults(EventResult promotionEventResult, List<Cart> carts) {
        Map<String, Integer> promotionEventMap = promotionEventResult.getPromotionEventResult();
        return calculatePresentationResults(carts, promotionEventMap);
    }

    private List<PresentationResult> calculatePresentationResults(List<Cart> carts,
                                                                  Map<String, Integer> promotionEventResult) {
        List<PresentationResult> presentationResults = new ArrayList<>();
        for (Cart cart : carts) {
            Integer i = promotionEventResult.get(cart.getName());
            if (i != null) {
                PresentationResult presentationResult = new PresentationResult(cart.getName(), i);
                presentationResults.add(presentationResult);
            }
        }
        return presentationResults;
    }

    private int getTotalPrice(List<Cart> carts, Products products) {
        int totalPrice = 0;
        for (Cart cart : carts) {
            int quantity = cart.getQuantity();
            int price = productManager.getPriceByName(products, cart.getName());
            totalPrice += quantity * price;
        }
        return totalPrice;
    }

    private void getReceipt(List<BuyResult> buyResults, List<PresentationResult> presentationResults,
                            PriceResult priceResult) {
        Receipt receipt = new Receipt(buyResults, presentationResults, priceResult);
        outputView.printReceipt(receipt);
    }

    private void createReceipt(int totalPrice, EventResult promotionEventResult, int membershipDiscount,
                               List<BuyResult> buyResults, List<PresentationResult> presentationResults) {
        PriceResult priceResult = new PriceResult(totalPrice, promotionEventResult.getGiftCount(), membershipDiscount);
        getReceipt(buyResults, presentationResults, priceResult);
    }

    private void continueRetry() {
        String choice = tryReadReplay();
        if (choice.equals(YES_CHOICE)) {
            System.out.println();
            run();
        }
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

    private String tryReadEnoughPromotionQuantity(Cart cart, int nonPromotionQuantity) {
        while (true) {
            try {
                String choice = inputView.readEnoughPromotionQuantity(cart.getName(), nonPromotionQuantity);
                validateYesOrNo(choice);
                return choice;
            } catch (IllegalArgumentException exception) {
                outputView.printErrorMessage(exception.getMessage());
            }
        }
    }

    private String tryReadGift(Cart cart) {
        while (true) {
            try {
                String choice = inputView.readGift(cart.getName());
                validateYesOrNo(choice);
                return choice;
            } catch (IllegalArgumentException exception) {
                outputView.printErrorMessage(exception.getMessage());
            }
        }
    }

    private String tryReadMembership() {
        while (true) {
            try {
                String choice = inputView.readMemberShip();
                validateYesOrNo(choice);
                return choice;
            } catch (IllegalArgumentException exception) {
                outputView.printErrorMessage(exception.getMessage());
            }
        }
    }

    private String tryReadReplay() {
        while (true) {
            try {
                String choice = inputView.readReplay();
                validateYesOrNo(choice);
                return choice;
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

    private void validateYesOrNo(String choice) {
        if (!choice.equals(YES_CHOICE) && !choice.equals(NO_CHOICE)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
    }
}
