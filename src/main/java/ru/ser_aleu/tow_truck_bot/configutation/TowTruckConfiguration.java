package ru.ser_aleu.tow_truck_bot.configutation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@Slf4j
@Configuration
public class TowTruckConfiguration {

    @Value("${telegram.forbidden-words.path}")
    private String telegramForbiddenWordsPath;

    @Value("${telegram.black-listed-telegram-names.path}")
    private String telegramBlackListedTelegramNamesPath;

    @Bean("forbiddenWords")
    public List<String> forbiddenWords() {
        List<String> forbiddenWords = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(telegramForbiddenWordsPath)))) {
            while (reader.ready()) {
                forbiddenWords.add(reader.readLine());
            }
        } catch (Exception e) {
            log.error("Error while forbidden words reading. {}", getStackTrace(e));
        }
        return forbiddenWords;
    }

    @Bean("blackListedTelegramNames")
    public List<String> blackListedTelegramNames() {
        List<String> forbiddenWords = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(telegramBlackListedTelegramNamesPath)))) {
            while (reader.ready()) {
                forbiddenWords.add(reader.readLine());
            }
        } catch (Exception e) {
            log.error("Error while black listed telegram names reading. {}", getStackTrace(e));
        }
        return forbiddenWords;
    }
}
