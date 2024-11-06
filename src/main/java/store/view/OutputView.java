package store.view;

import store.model.Products;

public class OutputView {

    public void printWellComeMessage() {
        System.out.println("안녕하세요. W편의점입니다.");
    }

    public void printProducts(Products products) {
        System.out.println("현재 보유하고 있는 상품입니다." + System.lineSeparator());

        String formatterProducts = OutputFormatter.formatterProducts(products);
        System.out.println(formatterProducts);
    }
}
