package store.controller;

import store.filereader.FileReader;
import store.model.Products;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private final InputView inputView;
    private final OutputView outputView;

    public StoreController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        outputView.printWellComeMessage();
        Products products = FileReader.readerFile();
        outputView.printProducts(products);

        String[] items = inputView.readItem();
    }
}
