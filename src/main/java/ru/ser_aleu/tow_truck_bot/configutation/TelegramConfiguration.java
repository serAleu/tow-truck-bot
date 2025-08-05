package ru.ser_aleu.tow_truck_bot.configutation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class TelegramConfiguration {

    @Value("${telegram.steps.questions.vehicle-type.buttons}")
    private String telegramVehicleTypeButtons;

    @Value("${telegram.web.auth.token}")
    private String botToken;

    @Bean("telegramClient")
    public TelegramClient telegramClient() {
        return new OkHttpTelegramClient(botToken);
    }

    @Bean("vehicleTypeKeyboard")
    public InlineKeyboardMarkup vehicleTypeKeyboard() {
        return createVehicleTypeKeyboard("VEHICLE_TYPE_");
    }

    private InlineKeyboardMarkup createVehicleTypeKeyboard(String callbackPrefix) {
        return InlineKeyboardMarkup.builder()
                .keyboard(
                        parseButtonNamesToStringList(telegramVehicleTypeButtons)
                                .stream()
                                .map(text -> new InlineKeyboardRow(List.of(
                                        InlineKeyboardButton.builder()
                                                .text(text)
                                                .callbackData(callbackPrefix + text.hashCode())
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
