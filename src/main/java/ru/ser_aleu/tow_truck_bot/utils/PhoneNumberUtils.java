package ru.ser_aleu.tow_truck_bot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class PhoneNumberUtils {

    private static final Logger log = LoggerFactory.getLogger(PhoneNumberUtils.class);

    /**
     * Проверяет валидность российского номера телефона
     *
     * @param phoneNumber номер телефона в любом формате
     * @return true если номер валидный
     */
    public static boolean isValidRussianPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }

        // Удаляем все нецифровые символы
        String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");

        // Проверяем базовые требования к номеру
        if (digitsOnly.length() != 11) {
            return false;
        }

        // Проверяем начало номера (8 или +7 -> заменяем на 7)
        if (!digitsOnly.startsWith("7") && !digitsOnly.startsWith("8")) {
            return false;
        }

        // Если номер начинается с 8, заменяем на 7 для единообразия
        if (digitsOnly.startsWith("8")) {
            digitsOnly = "7" + digitsOnly.substring(1);
        }

        // Проверяем что код оператора начинается с 9
        if (digitsOnly.charAt(1) != '9') {
            return false;
        }

        // Дополнительные проверки:
        // 1. Проверяем что код оператора валидный (9xx)
        String operatorCode = digitsOnly.substring(1, 4);
        if (!operatorCode.matches("9[0-9]{2}")) {
            return false;
        }

        // 2. Проверяем что остальные цифры не все одинаковые (например, 79999999999)
        String finalDigitsOnly = digitsOnly;
        if (digitsOnly.chars().allMatch(c -> c == finalDigitsOnly.charAt(0))) {
            return false;
        }

        return true;
    }

    /**
     * Форматирует валидный номер телефона в стандартный вид +7(XXX)XXX-XX-XX
     *
     * @param phoneNumber валидный номер телефона
     * @return отформатированная строка
     * @throws IllegalArgumentException если номер невалидный
     */
    public static String formatRussianPhoneNumber(String phoneNumber) {
        try {
            if (!isValidRussianPhoneNumber(phoneNumber)) {
                throw new IllegalArgumentException("Invalid Russian phone number: " + phoneNumber);
            }

            // Нормализуем номер (оставляем только цифры)
            String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");

            // Если номер начинается с 8, заменяем на +7
            if (digitsOnly.startsWith("8")) {
                digitsOnly = "7" + digitsOnly.substring(1);
            }

            // Форматируем по шаблону +7(XXX)XXX-XX-XX
            return String.format("+7(%s)%s-%s-%s",
                    digitsOnly.substring(1, 4),
                    digitsOnly.substring(4, 7),
                    digitsOnly.substring(7, 9),
                    digitsOnly.substring(9, 11));
        } catch (Exception e) {
            log.error("Error while phone-num formating. {}", getStackTrace(e));
            return null;
        }
    }
}
