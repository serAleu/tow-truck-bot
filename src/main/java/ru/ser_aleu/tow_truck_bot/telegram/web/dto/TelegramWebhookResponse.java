package ru.ser_aleu.tow_truck_bot.telegram.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramWebhookResponse {
    private boolean ok;
    private boolean result;
    private String description;
    private String errorCode;

    @JsonProperty("ok")
    public boolean isOk() {
        return ok;
    }

    @JsonProperty("result")
    public boolean isResult() {
        return result;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }
}
