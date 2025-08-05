package ru.ser_aleu.tow_truck_bot.telegram.enums.selects;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Selectable<T extends Enum<T> & Selectable<T>> {

    String getDisplayName();

    default Map<String, T> getDescriptionToEnumMap() {
        return Arrays.stream(getEnumClass().getEnumConstants())
                .collect(Collectors.toMap(
                        T::getDisplayName,
                        Function.identity()
                ));
    }

    default T fromDescription(String description) {
        T result = getDescriptionToEnumMap().get(description);
        if (result == null) {
            throw new IllegalArgumentException("Unknown description: " + description);
        }
        return result;
    }

    default String getAllDescriptionsAsString(String delimiter) {
        return Arrays.stream(getEnumClass().getEnumConstants())
                .map(T::getDisplayName)
                .collect(Collectors.joining(delimiter));
    }

    default T parseFromString(String input, String prefix) {
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
