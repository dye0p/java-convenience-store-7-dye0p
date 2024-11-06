package store.view;

public class InputConverter {

    public static String[] converter(String input) {
        String items = input.replaceAll("[\\[\\]]", "");
        return items.split(",");
    }
}
