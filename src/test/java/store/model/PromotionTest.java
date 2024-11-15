package store.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PromotionTest {

    @DisplayName("오늘이 프로모션 기간 내에 속한다면 true를 반환한다.")
    @Test
    void isWithinPromotionDate() {
        //given
        Promotion promotion = new Promotion("탄산2+1", 10, 10,
                LocalDate.of(2024, 1, 1).atStartOfDay(),
                LocalDate.of(2024, 12, 31).atStartOfDay()
        );

        LocalDateTime now = LocalDate.of(2024, 11, 10).atStartOfDay();

        //when
        boolean result = promotion.isWithinPromotionDate(now);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("오늘이 프로모션 기간 내에 속하지 않는다면 false를 반환한다.")
    @Test
    void isNotWithinPromotionDate() {
        //given
        Promotion promotion = new Promotion("탄산2+1", 10, 10,
                LocalDate.of(2024, 1, 1).atStartOfDay(),
                LocalDate.of(2024, 12, 31).atStartOfDay()
        );

        LocalDateTime now = LocalDate.of(2023, 12, 31).atStartOfDay();

        //when
        boolean result = promotion.isWithinPromotionDate(now);

        //then
        assertThat(result).isFalse();
    }
}
