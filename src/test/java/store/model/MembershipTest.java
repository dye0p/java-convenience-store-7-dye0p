package store.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MembershipTest {

    @DisplayName("멤버십 할인 금액을 계산할 수 있다.")
    @Test
    void calculateDiscount() {
        //given
        int nonPromotionPrice = 10000;
        Membership membership = new Membership();

        //when
        int discountPrice = membership.calculateDiscount(nonPromotionPrice);

        //then
        assertThat(discountPrice).isEqualTo(3000);
    }

    @DisplayName("멤버십 할인의 최대 할인 금액은 8000원이다.")
    @Test
    void maxDiscountPrice() {
        //given
        int nonPromotionPrice = 30000;
        Membership membership = new Membership();

        //when
        int discountPrice = membership.calculateDiscount(nonPromotionPrice);

        //then
        assertThat(discountPrice).isEqualTo(8000);
    }
}
