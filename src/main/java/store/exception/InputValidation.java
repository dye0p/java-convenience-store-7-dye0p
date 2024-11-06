package store.exception;

public final class InputValidation {

    private InputValidation() {
    }

    public static void validateNullOrBlank(String input) {
        if (input == null || input.contains(" ")) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
    }

    public static void validateFormat(String input) {
        if (!input.startsWith("[") || !input.endsWith("]")) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT_FORMAT.getMessage());
        }
    }

    public static void validateSize(String[] productAndQuantity) {
        if (productAndQuantity.length != 2) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT_FORMAT.getMessage());
        }
    }
}
