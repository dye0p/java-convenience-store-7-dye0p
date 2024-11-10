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
                giftCount = todayPromotionAvailable(cart, promotionProduct, giftCount, promotionEventResult);
            }
        } else {
            // 일반 상품은 프로모션 혜택이 없음
            Product basicProduct = productManager.getBasicByName(cart.getName());
            nonPromotionItems.put(cart, basicProduct.getPrice());
            basicProduct.deductQuantity(cart.getQuantity()); //재고 변경
        }
        return giftCount;
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
        giftCount = notEnoughPromotionQuantity(cart, promotionProduct, giftCount, promotionEventResult);

        // 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받는다
        if (cart.getQuantity() <= promotionProduct.getQuantity() && !promotionManager.isNotEnoughPromotionQuantity(
                cart.getQuantity(), promotionProduct)) {
            giftCount = handleAddGift(cart, promotionProduct, giftCount, promotionEventResult);
        }

        //아무조건도 안걸릴때
        else if (cart.getQuantity() <= promotionProduct.getQuantity() && promotionManager.isNotEnoughPromotionQuantity(
                cart.getQuantity(), promotionProduct)) {
            //수입량 수량에 따라서 받을 수 있는 증정 개수 계산
            int freeCount = getGiftCount(cart, promotionProduct);
            promotionEventResult.put(cart.getName(), freeCount);
            giftCount += promotionProduct.getPrice() * freeCount;
            //재고 차감
            promotionProduct.deductQuantity(cart.getQuantity());
        }

        return giftCount;
    }

    private int notEnoughPromotionQuantity(Cart cart, Product promotionProduct, int giftCount,
                                           Map<String, Integer> promotionEventResult) {
        if (cart.getQuantity() > promotionProduct.getQuantity()) {
            int nonPromotionQuantity = promotionManager.notPromotionByQuantity(cart.getQuantity(), promotionProduct);
            giftCount = selectPromotionQuantity(cart, nonPromotionQuantity, promotionProduct, promotionEventResult,
                    giftCount);
        }
        return giftCount;
    }

    private int selectPromotionQuantity(Cart cart, int nonPromotionQuantity, Product promotionProduct,
                                        Map<String, Integer> promotionEventResult, int giftCount) {
        String choice = inputView.readEnoughPromotionQuantity(cart.getName(), nonPromotionQuantity);
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
        //받을 수 있는 증정 개수 담기
        int maxGiftCount = promotionManager.calculateGiftCount(promotionProduct);
        promotionEventResult.put(cart.getName(), maxGiftCount);
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

    private int handleAddGift(Cart cart, Product promotionProduct, int giftCount,
                              Map<String, Integer> promotionEventResult) {
        String choice = inputView.readGift(cart.getName());
        if (choice.equals(YES_CHOICE)) {
            giftCount = plusGiftCount(cart, promotionProduct, giftCount, promotionEventResult);
        }

        return giftCount;
    }

    private int plusGiftCount(Cart cart, Product promotionProduct, int giftCount,
                              Map<String, Integer> promotionEventResult) {
        //추가 증정할인이 들어간다.
        //1개만 추가되는것이 아니라 기존의 증정에서 1개가 추가되는 것이다.
        int existingGiftCount = getGiftCount(cart, promotionProduct);
        giftCount += promotionProduct.getPrice();

        //한개를 더 받으면 해당 상품의 수량에 하나를 더 추가한다.
        cart.plusGiftQuantity();
        promotionEventResult.put(cart.getName(), existingGiftCount + 1);

        //재고 차감
        promotionProduct.deductQuantity(cart.getQuantity());
        return giftCount;
    }

    private int getGiftCount(Cart cart, Product promotionProduct) {
        return promotionProduct.calculatePromotionGift(cart);
    }

    private int membershipEvent(EventResult eventResult) {
        String choice = inputView.readMemberShip();
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
                                                                  Map<String, Integer> promotionEventMap) {
        List<PresentationResult> presentationResults = new ArrayList<>();
        for (Cart cart : carts) {
            Integer i = promotionEventMap.get(cart.getName());
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
            int quantity = cart.getQuantity(); //수량
            int price = productManager.getPriceByName(products, cart.getName()); //가격

            totalPrice += quantity * price;
        }
        return totalPrice;
    }

    private void getReceipt(List<BuyResult> buyResults, List<PresentationResult> presentationResults,
                            PriceResult priceResult) {
        //영수증 생성 후 출력
        Receipt receipt = new Receipt(buyResults, presentationResults, priceResult);
        outputView.printReceipt(receipt);
    }

    private void createReceipt(int totalPrice, EventResult promotionEventResult, int membershipDiscount,
                               List<BuyResult> buyResults, List<PresentationResult> presentationResults) {
        PriceResult priceResult = new PriceResult(totalPrice, promotionEventResult.getGiftCount(), membershipDiscount);
        getReceipt(buyResults, presentationResults, priceResult);
    }

    private void continueRetry() {
        String choice = inputView.readReplay();
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
