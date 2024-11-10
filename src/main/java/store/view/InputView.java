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

    public String readEnoughPromotionQuantity(String name, int nonPromotionQuantity) {
        String format = String.format("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)",
                name, nonPromotionQuantity);
        System.out.println(NEXT_LINE + format);

        return Console.readLine();
    }
}
