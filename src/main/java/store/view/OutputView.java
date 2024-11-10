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
        System.out.print(message);
    }

    public void printReceipt(Receipt receipt) {
        System.out.println(NEXT_LINE + "==============W 편의점================");
        buyResultFormating(receipt);
        presentationResultFormating(receipt);
        priceResultFormating(receipt);
    }

    private void buyResultFormating(Receipt receipt) {
        List<BuyResult> buyResult = receipt.getBuyResult();
        System.out.println("상품명\t\t\t\t수량\t\t\t금액");
        for (BuyResult result : buyResult) {
            System.out.println(result.getName() + "\t\t\t\t\t" + result.getQuantity() + "\t\t" + String.format("%,d",
                    (result.getPrice()) * result.getQuantity()));
        }
    }

    private void presentationResultFormating(Receipt receipt) {
        List<PresentationResult> presentationResult = receipt.getPresentationResult();
        System.out.println("=============증\t\t정===============");
        for (PresentationResult result : presentationResult) {
            System.out.println(result.getName() + "\t\t\t" + result.getQuantity());
        }
    }

    private void priceResultFormating(Receipt receipt) {
        System.out.println("====================================");
        System.out.println("총구매액" + "\t\t\t" + String.format("%,d", receipt.getPriceResult().getTotal()));
        System.out.println("행사할인" + "\t\t\t" + String.format("-%,d", receipt.getPriceResult().getPresentation()));
        System.out.println("멤버십할인" + "\t\t\t" + String.format("-%,d", receipt.getPriceResult().getMembership()));
        System.out.println("내실돈" + "\t\t\t" + String.format("%,d", receipt.getPriceResult().finalPayment()));
    }
}
