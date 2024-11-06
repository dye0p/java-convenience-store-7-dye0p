package store.exception;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class InputValidationTest {

    @DisplayName("null 또는 공백을 포함된 문자열이면 예외를 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {" ", "[콜라 - 10],[사이다 - 3]", "[콜라-10], [사이다-3]"})
    void validateNullOrBlank(String input) {
        assertThatThrownBy(() -> InputValidation.validateNullOrBlank(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_INPUT.getMessage());
    }

    @DisplayName("입력한 상품의 형식이 '[' 로 시작해 ']' 로 끝나지 않는 경우 예외를 발생한다.")
    @Test
    void validateFormat() {
        //given
        String input = "]콜라-10],}사이다-3]";

        //when //then
        assertThatThrownBy(() -> InputValidation.validateFormat(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_INPUT_FORMAT.getMessage());
    }

    @DisplayName("상품 이름과 수량이 하나라도 누락된 경우 예외를 발생한다.")
    @Test
    void validateSize() {
        //given
        String[] productAndQuantity = {"콜라"};

        //when //then
        assertThatThrownBy(() -> InputValidation.validateSize(productAndQuantity));
    }
}
