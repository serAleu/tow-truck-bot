package ru.ser_aleu.tow_truck_bot.configutation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.ser_aleu.tow_truck_bot.telegram.enums.selects.Selectable;
import ru.ser_aleu.tow_truck_bot.telegram.enums.selects.VehicleProblemType;
import ru.ser_aleu.tow_truck_bot.telegram.enums.selects.VehicleType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class TelegramConfiguration {

    private static final String DELIMITER = ";";

    @Value("${telegram.web.auth.token}")
    private String botToken;

    @Bean("telegramClient")
    public TelegramClient telegramClient() {
        return new OkHttpTelegramClient(botToken);
    }

    @Bean("vehicleTypeKeyboard")
    public InlineKeyboardMarkup vehicleTypeKeyboard() {
        return createKeyboard("VEHICLE_TYPE=", VehicleType.class);
    }

    @Bean("vehicleProblemTypeKeyboard")
    public InlineKeyboardMarkup vehicleProblemTypeKeyboard() {
        return createKeyboard("VEHICLE_PROBLEM_TYPE=", VehicleProblemType.class);
    }

    private <T extends Enum<T> & Selectable<T>> InlineKeyboardMarkup createKeyboard(String callbackPrefix, Class<T> enumClass) {
        return InlineKeyboardMarkup.builder()
                .keyboard(
                        parseButtonNamesToStringList(Selectable.getAllDisplayNamesAsString(enumClass, DELIMITER))
                                .stream()
                                .map(text -> new InlineKeyboardRow(List.of(
                                        InlineKeyboardButton.builder()
                                                .text(text)
                                                .callbackData(callbackPrefix + Selectable.getEnumByDisplayName(enumClass, text))
                                                .build()
                                )))
                                .collect(Collectors.toList())
                )
                .build();
    }

    private List<String> parseButtonNamesToStringList(String telegramVehicleTypeButtons) {
        return Arrays.stream(telegramVehicleTypeButtons.split(";"))
                .filter(text -> !text.isEmpty())
                .map(String::trim)
                .toList();
    }
}
