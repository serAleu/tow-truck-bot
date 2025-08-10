package ru.ser_aleu.tow_truck_bot.events.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.ser_aleu.tow_truck_bot.events.dto.*;
import ru.ser_aleu.tow_truck_bot.exceptions.EventProcessingException;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUser;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUserSessionRegistry;
import ru.ser_aleu.tow_truck_bot.telegram.enums.ChatState;
import ru.ser_aleu.tow_truck_bot.telegram.service.TelegramService;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@Component
@Slf4j
@RequiredArgsConstructor
public class TowTruckEventsListener {

    private final TelegramService telegramService;
    private final TelegramUserSessionRegistry telegramUserSessionRegistry;

    @Async("eventTaskExecutor")
    @EventListener
    public void handleBotStartedEvent(BotStartedEvent event) {
        try {
            TelegramUser telegramUser = event.getTelegramUser();
            telegramService.sendStartReply(telegramUser);
            telegramUser.setCurrentChatState(ChatState.START);
            telegramUserSessionRegistry.updateUser(telegramUser, ChatState.START.toString());
            telegramService.sendPhoneNumRequest(telegramUser);
            telegramUser.setCurrentChatState(ChatState.AWAITING_USER_REGISTERED);
            telegramUserSessionRegistry.updateUser(telegramUser, ChatState.AWAITING_USER_REGISTERED.toString());
            log.info("Processing bot started event is finished: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process bot started event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Bot started event processing failed.", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleReRequestPhoneNumEvent(ReRequestPhoneNumEvent event) {
        try {
            TelegramUser telegramUser = event.getTelegramUser();
            telegramUserSessionRegistry.updateUser(telegramUser, ChatState.START.toString());
            telegramService.sendIncorrectPhoneNumNotification(telegramUser);
            telegramService.sendPhoneNumRequest(telegramUser);
            telegramUser.setCurrentChatState(ChatState.AWAITING_USER_REGISTERED);
            telegramUserSessionRegistry.updateUser(telegramUser, ChatState.AWAITING_USER_REGISTERED.toString());
            log.info("Processing bot re-requesting phone-num event is finished: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process re-requesting phone-num event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Re-requesting phone-num event processing failed.", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        try {
            TelegramUser telegramUser = event.getTelegramUser();
            telegramUser.setPhoneNumber(event.getPhoneNum());
            telegramUserSessionRegistry.updateUser(telegramUser, "Phone num: " + telegramUser.getPhoneNumber());
            telegramService.sendVehicleTypeRequest(telegramUser);
            telegramUser.setCurrentChatState(ChatState.AWAITING_VEHICLE_TYPE_SELECTION);
            telegramUserSessionRegistry.updateUser(telegramUser, ChatState.AWAITING_VEHICLE_TYPE_SELECTION.toString());
            log.info("Processing user registered event is finished: chat_id = {}, telegram_user = {}", telegramUser, telegramUser.getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process user registered event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("User registered processing failed", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleVehicleTypeSelectedEvent(VehicleTypeSelectedEvent event) {
        try {
            TelegramUser telegramUser = event.getTelegramUser();
            telegramUser.setVehicleType(event.getVehicleType());
            telegramUserSessionRegistry.updateUser(telegramUser, event.getVehicleType().toString());
            telegramService.sendVehicleProblemRequest(telegramUser);
            telegramUser.setCurrentChatState(ChatState.AWAITING_VEHICLE_TYPE_PROBLEM_SELECTION);
            telegramUserSessionRegistry.updateUser(telegramUser, ChatState.AWAITING_VEHICLE_TYPE_PROBLEM_SELECTION.toString());
            log.info("Processing vehicle type selected event is finished: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process vehicle type selected event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Vehicle type selected processing failed.", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleVehicleProblemTypeSelectedEvent(VehicleProblemTypeSelectedEvent event) {
        try {
            TelegramUser telegramUser = event.getTelegramUser();
            telegramUser.setVehicleProblemType(event.getVehicleProblemType());
            telegramUserSessionRegistry.updateUser(telegramUser, event.getVehicleProblemType().toString());
            telegramService.sendRequestCurrentLocation(event.getTelegramUser());
            telegramUser.setCurrentChatState(ChatState.AWAITING_CURRENT_LOCATION_PROVIDING);
            telegramUserSessionRegistry.updateUser(telegramUser, ChatState.AWAITING_CURRENT_LOCATION_PROVIDING.toString());
            log.info("Processing vehicle problem type selected event is finished: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process vehicle problem type selected event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Vehicle problem type selected processing failed.", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleCurrentLocationProvidedEvent(CurrentLocationProvidedEvent event) {
        try {
            TelegramUser telegramUser = event.getTelegramUser();
            telegramUser.setCurrentLocation(event.getTelegramUserLocation());
            telegramUserSessionRegistry.updateUser(telegramUser, telegramUser.getCurrentLocation().toString());
            telegramService.sendRequestDestinationLocation(event.getTelegramUser());
            telegramUser.setCurrentChatState(ChatState.AWAITING_DESTINATION_LOCATION_PROVIDING);
            telegramUserSessionRegistry.updateUser(telegramUser, ChatState.AWAITING_DESTINATION_LOCATION_PROVIDING.toString());
            log.info("Processing current location provided event is finished: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process current location provided event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Current location providing processing failed", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleDestinationLocationProvidedEvent(DestinationLocationProvidedEvent event) {
        try {
            TelegramUser telegramUser = event.getTelegramUser();
            telegramUser.setDestinationLocation(event.getTelegramUserLocation());
            telegramUserSessionRegistry.updateUser(telegramUser, telegramUser.getDestinationLocation().toString());
            telegramService.sendPaymentMethodRequest(event.getTelegramUser());
            telegramUser.setCurrentChatState(ChatState.AWAITING_PAYMENT_METHOD_PROVIDING);
            telegramUserSessionRegistry.updateUser(telegramUser, ChatState.AWAITING_PAYMENT_METHOD_PROVIDING.toString());
            log.info("Processing destination location provided event is finished: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process destination location provided event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Destination location providing processing failed", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handlePaymentMethodProvidedEvent(PaymentMethodProvidedEvent event) {
        try {
            TelegramUser telegramUser = event.getTelegramUser();
            telegramUser.setPaymentMethodType(event.getPaymentMethodType());
            telegramUserSessionRegistry.updateUser(telegramUser, telegramUser.getPaymentMethodType().toString());
            telegramService.sendNeedDocsRequest(event.getTelegramUser());
            telegramUser.setCurrentChatState(ChatState.AWAITING_NEED_DOCS_PROVIDING);
            telegramUserSessionRegistry.updateUser(telegramUser, ChatState.AWAITING_NEED_DOCS_PROVIDING.toString());
            log.info("Processing payment method provided event is finished: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process payment method provided event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Payment method providing processing failed", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleNeedDocsReplyProvidedEvent(NeedDocsReplyProvidedEvent event) {
        try {
            TelegramUser telegramUser = event.getTelegramUser();
            telegramUser.setNeedDocs(event.getYesOrNo());
            telegramUserSessionRegistry.updateUser(telegramUser, "Need docs = " + telegramUser.getNeedDocs().toString());
            telegramService.sendOrderConfirmation(event.getTelegramUser());
            telegramUser.setCurrentChatState(ChatState.AWAITING_ORDER_CONFIRMATION);
            telegramUserSessionRegistry.updateUser(telegramUser, ChatState.AWAITING_ORDER_CONFIRMATION.toString());
            log.info("Processing need docs provided event is finished: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process need docs provided event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Need docs providing processing failed", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleOrderConfirmedEvent(OrderConfirmedEvent event) {
        try {
            TelegramUser telegramUser = event.getTelegramUser();
            telegramUser.setOrderConfirmed(true);
            telegramUserSessionRegistry.updateUser(telegramUser, ChatState.ORDER_CONFIRMED.toString());
            telegramService.sendOrderToAdmin(telegramUser);
            telegramService.sendFinalReply(telegramUser);
            log.info("Processing order confirmed event: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process order confirmed event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Order confirmed processing failed", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleTextMessageEvent(TextMessageEvent event) {
        try {
            telegramService.processTextRequest(event.getTelegramUser());
            log.info("Processing text message event is finished: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to text message notification event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Text message notification failed", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleFraudDetectionEvent(FraudDetectionEvent event) {
        try {
            log.info("Processing fraud detection event: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process fraud detection event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Fraud detection processing failed.", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleOrderCalculationEvent(OrderCalculationEvent event) {
        try {
            log.info("Processing order calculation event: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process order calculation event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Order calculation processing failed.", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleOrderSentToOperatorEvent(OrderSentToOperatorEvent event) {
        try {
            log.info("Processing order sent to operator event: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process order sent to operator event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Order sent to operator processing failed", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleErrorAdminNotificationEvent(ErrorAdminNotificationEvent event) {
        try {
            log.info("Processing error admin notification event: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to error admin notification event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Error admin notification failed", e);
        }
    }
}
