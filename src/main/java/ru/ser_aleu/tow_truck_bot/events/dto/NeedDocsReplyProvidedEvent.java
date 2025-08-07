package ru.ser_aleu.tow_truck_bot.events.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUser;
import ru.ser_aleu.tow_truck_bot.telegram.enums.selects.YesOrNo;

@Getter
@Setter
@Accessors(chain = true)
public class NeedDocsReplyProvidedEvent extends BaseEvent {

    private YesOrNo yesOrNo;

    public NeedDocsReplyProvidedEvent(TelegramUser telegramUser, YesOrNo yesOrNo) {
        super(telegramUser);
        this.yesOrNo = yesOrNo;
    }
}