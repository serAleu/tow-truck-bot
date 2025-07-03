package ru.ser_aleu.tow_truck_bot.configutation;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@Slf4j
@Configuration
public class TowTruckApplicationConfiguration {

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

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }
}
