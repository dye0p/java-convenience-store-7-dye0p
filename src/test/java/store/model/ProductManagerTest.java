package store.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.exception.ErrorMessage;

class ProductManagerTest {

    @DisplayName("해당하는 상품의 모든 재고의 합계를 계산한다..")
    @Test
    void sumProductQuantityByName() {
        //given
        Product product = new Product("콜라", 1000, 10, "탄산2+1");
        Product product1 = new Product("콜라", 1000, 10, null);

        Products products = new Products(List.of(product, product1));

        ProductManager productManager = new ProductManager(products);

        //when
        int quantity = productManager.sumProductQuantityByName("콜라");

        //then
        assertThat(quantity).isEqualTo(20);
    }

    @DisplayName("구입한 상품이 존재하지 않는 상품이라면 예외를 발생한다.")
    @Test
    void nonExistent() {
        //given
        Product product = new Product("콜라", 1000, 10, "탄산2+1");
        Product product1 = new Product("콜라", 1000, 10, null);

        Products products = new Products(List.of(product, product1));

        ProductManager productManager = new ProductManager(products);

        //when //then
        assertThatThrownBy(() -> productManager.validateExistent("감자칩"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_FOUNT_PRODUCT.getMessage());
    }

    @DisplayName("구입한 상품이 존재하는 상품이라면 예외를 발생하지 않는다.")
    @Test
    void existent() {
        //given
        Product product = new Product("콜라", 1000, 10, "탄산2+1");
        Product product1 = new Product("콜라", 1000, 10, null);

        Products products = new Products(List.of(product, product1));

        ProductManager productManager = new ProductManager(products);

        //when //then
        assertThatCode(() -> productManager.validateExistent("콜라"))
                .doesNotThrowAnyException();
    }

    @DisplayName("구입한 상품이 프로모션 상품이라면 true를 반환한다.")
    @Test
    void isPromotionProduct() {
        //given
        Product product = new Product("콜라", 1000, 10, "탄산2+1");
        Product product1 = new Product("감자칩", 1000, 10, null);

        Products products = new Products(List.of(product, product1));

        ProductManager productManager = new ProductManager(products);

        //when
        boolean result = productManager.isPromotionProduct("콜라");

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("구입한 상품이 프로모션 상품이 아니라면 false를 반환한다.")
    @Test
    void isNotPromotionProduct() {
        //given
        Product product = new Product("콜라", 1000, 10, "탄산2+1");
        Product product1 = new Product("감자칩", 1000, 10, null);

        Products products = new Products(List.of(product, product1));

        ProductManager productManager = new ProductManager(products);

        //when
        boolean result = productManager.isPromotionProduct("감자칩");

        //then
        assertThat(result).isFalse();
    }
}
