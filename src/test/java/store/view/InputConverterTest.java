package store.view;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.Cart;

class InputConverterTest {

    @DisplayName("입력한 상품과 수량으로 Cart 리스트를 만들 수 있다.")
    @Test
    void re() {
        //given
        String input = "[콜라-10],[사이다-3]";

        //when
        List<Cart> carts = InputConverter.converter(input);

        //then
        assertThat(carts).hasSize(2);
    }
}
