package store.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @DisplayName("같은 이름을 가진 상품이라면 true를 반환한다.")
    @Test
    void isSameName() {
        //given
        String name = "콜라";

        Product product = new Product("콜라", 1000, 10, "탄산2+1");

        //when
        boolean result = product.isSameName(name);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("상품이 일반 상품이라면 true를 반환한다.")
    @Test
    void isRegular() {
        //given
        Product product = new Product("콜라", 1000, 10, null);

        //when
        boolean result = product.isRegular();

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("재고가 0개가 아니라면 true를 반환한다.")
    @Test
    void isInStock() {
        //given
        Product product = new Product("콜라", 1000, 1, null);

        //when
        boolean result = product.isInStock();

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("프로모션 상품이라면 true를 반환한다.")
    @Test
    void hasPromotion() {
        //given
        Product product = new Product("콜라", 1000, 1, "탄산2+1");

        //when
        boolean result = product.hasPromotion();

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("지정된 개수만큼 재고 수량을 차감한다.")
    @Test
    void deductQuantity() {
        //given
        int deductQuantity = 3;
        Product product = new Product("콜라", 1000, 5, "탄산2+1");

        //when
        product.deductQuantity(deductQuantity);

        //then
        assertThat(product.getQuantity()).isEqualTo(2);
    }

    @DisplayName("프로모션 상품의 프로모션에 따라서 증정받을 수 있는 증정 개수를 계산한다.")
    @Test
    void calculatePromotionGift() {
        //given
        Cart cart = new Cart("콜라", 4);
        Product product = new Product("콜라", 1000, 5, "탄산2+1");

        //when
        int giftCount = product.calculatePromotionGift(cart);

        //then
        assertThat(giftCount).isEqualTo(1);
    }
}
