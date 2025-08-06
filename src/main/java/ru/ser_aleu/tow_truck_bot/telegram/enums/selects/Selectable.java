package ru.ser_aleu.tow_truck_bot.telegram.enums.selects;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Selectable<T extends Enum<T> & Selectable<T>> {

    String getDisplayName();

    static <T extends Enum<T> & Selectable<T>> String getAllDisplayNamesAsString(Class<T> enumClass, String delimiter) {
        return enumClass.getEnumConstants()[0].getAllDisplayNamesAsString(delimiter);
    }

    static <T extends Enum<T> & Selectable<T>> T getEnumByDisplayName(Class<T> enumClass, String displayName) {
        return enumClass.getEnumConstants()[0].getEnumByDisplayName(displayName);
    }

    default Map<String, T> getDisplayNameToEnumMap() {
        return Arrays.stream(getEnumClass().getEnumConstants())
                .collect(Collectors.toMap(
                        T::getDisplayName,
                        Function.identity()
                ));
    }

    default T getEnumByDisplayName(String displayName) {
        T result = getDisplayNameToEnumMap().get(displayName);
        if (result == null) {
            throw new IllegalArgumentException("Unknown displayName: " + displayName);
        }
        return result;
    }

    default String getAllDisplayNamesAsString(String delimiter) {
        return Arrays.stream(getEnumClass().getEnumConstants())
                .map(T::getDisplayName)
                .collect(Collectors.joining(delimiter));
    }

    default T getEnumByDisplayNameWithPrefix(String input, String prefix) {
        if (input == null || !input.startsWith(prefix + "=")) {
            throw new IllegalArgumentException("Invalid input format. Expected '" + prefix + "=TYPE'");
        }

        String enumName = input.substring(prefix.length() + 1);
        try {
            return Enum.valueOf(getEnumClass(), enumName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown enum value: " + enumName);
        }
    }

    @SuppressWarnings("unchecked")
    default Class<T> getEnumClass() {
        return (Class<T>) this.getClass();
    }
}
