package store.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PromotionManagerTest {

    @DisplayName("특정 프로모션 기간에 오늘이 포함된다면 true를 반환한다.")
    @Test
    void isTodayPromotionAvailable() {
        //given
        List<Promotion> promotions = List.of(
                new Promotion("탄산2+1", 10, 10,
                        LocalDate.of(2024, 1, 1).atStartOfDay(),
                        LocalDate.of(2024, 12, 31).atStartOfDay())
        );

        PromotionManager promotionManager = new PromotionManager(promotions);

        LocalDateTime now = LocalDate.of(2024, 11, 10).atStartOfDay();

        //when
        boolean result = promotionManager.isTodayPromotionAvailable("탄산2+1", now);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("특정 프로모션 기간에 오늘이 포함되지 않는다면 false를 반환한다.")
    @Test
    void isNotTodayPromotionAvailable() {
        //given
        List<Promotion> promotions = List.of(
                new Promotion("반짝할인", 10, 10,
                        LocalDate.of(2024, 11, 1).atStartOfDay(),
                        LocalDate.of(2024, 11, 30).atStartOfDay())
        );

        PromotionManager promotionManager = new PromotionManager(promotions);

        LocalDateTime now = LocalDate.of(2024, 10, 31).atStartOfDay();

        //when
        boolean result = promotionManager.isTodayPromotionAvailable("반짝할인", now);

        //then
        assertThat(result).isFalse();
    }
}
