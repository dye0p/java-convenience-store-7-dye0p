package store.exception;

public enum ErrorMessage {

    ERROR_SIGNATURE("[ERROR] "),
    INVALID_INPUT_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요"),
    NOT_FOUNT_PRODUCT("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    EXCEED_STOCK_QUANTITY("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    INVALID_INPUT("잘못된 입력입니다. 다시 입력해 주세요.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return ERROR_SIGNATURE.message + this.message;
    }
}
