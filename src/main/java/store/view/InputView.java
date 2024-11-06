package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import store.model.Cart;

public class InputView {

    private static final String NEXT_LINE = System.lineSeparator();

    public List<Cart> readItem() {
        System.out.println(NEXT_LINE + "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String input = Console.readLine();

        return InputConverter.converter(input);
    }
}
