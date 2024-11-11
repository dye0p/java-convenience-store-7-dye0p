package store.view;

import java.util.List;
import store.model.Products;
import store.model.receipt.BuyResult;
import store.model.receipt.PresentationResult;
import store.model.receipt.Receipt;

public class OutputView {

    public static final String NEXT_LINE = System.lineSeparator();

    public void printWellComeMessage() {
        System.out.println("안녕하세요. W편의점입니다.");
    }

    public void printProducts(Products products) {
        System.out.println("현재 보유하고 있는 상품입니다." + NEXT_LINE);

        String formatterProducts = OutputFormatter.formatterProducts(products);
        System.out.println(formatterProducts);
    }

    public void printErrorMessage(String message) {
        System.out.println(message);
    }

    public void printReceipt(Receipt receipt) {
        System.out.println(NEXT_LINE + "==============W 편의점================");
        buyResultFormating(receipt);
        presentationResultFormating(receipt);
        priceResultFormating(receipt);
    }

    private void buyResultFormating(Receipt receipt) {
        List<BuyResult> buyResult = receipt.getBuyResult();
        System.out.printf("%-18s %-8s %s%n", "상품명", "수량", "금액");
        for (BuyResult result : buyResult) {
            System.out.printf("%-18s %-8d %,d%n", result.getName(), result.getQuantity(),
                    result.getQuantity() * result.getPrice());
        }
    }

    private void presentationResultFormating(Receipt receipt) {
        List<PresentationResult> presentationResult = receipt.getPresentationResult();
        System.out.println("=============증      정===============");
        for (PresentationResult result : presentationResult) {
            System.out.printf("%-18s %-8d%n", result.getName(), result.getQuantity());
        }
    }

    private void priceResultFormating(Receipt receipt) {
        System.out.println("====================================");
        System.out.printf("%-18s %-8d %,-1d%n", "총구매액", receipt.totalBuyQuantity(),
                receipt.getPriceResult().getTotal());

        String giftDiscountFormat = String.format("-%,d", receipt.getPriceResult().getPresentation());
        System.out.printf("%-27s %-1s%n", "행사할인", giftDiscountFormat);

        String membershipDiscountFormat = String.format("-%,d", receipt.getPriceResult().getMembership());
        System.out.printf("%-27s %-1s%n", "멤버십할인", membershipDiscountFormat);
        System.out.printf("%-27s %,-1d%n", "내실돈", receipt.getPriceResult().finalPayment());
    }
}
