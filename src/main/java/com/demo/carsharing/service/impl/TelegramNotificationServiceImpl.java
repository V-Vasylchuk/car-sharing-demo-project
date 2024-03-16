package com.demo.carsharing.service.impl;

import com.demo.carsharing.config.BotConfig;
import com.demo.carsharing.dto.response.RentalResponseDto;
import com.demo.carsharing.model.Rental;
import com.demo.carsharing.model.User;
import com.demo.carsharing.service.CarService;
import com.demo.carsharing.service.NotificationService;
import com.demo.carsharing.service.RentalService;
import com.demo.carsharing.service.UserService;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@AllArgsConstructor
public class TelegramNotificationServiceImpl extends TelegramLongPollingBot
        implements NotificationService {

    private final BotConfig botConfig;
    private final UserService userService;
    private final RentalService rentalService;
    private final CarService carService;

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            if (matchPattern(messageText)) {
                User user = userService.findByEmail(messageText).get();
                user.setChatId(String.valueOf(update.getMessage().getChatId()));
                userService.update(user);
                return;
            }
            switch (messageText) {
                case "/start":
                    startCommandReceived(String.valueOf(update.getMessage().getChatId()),
                            update.getMessage().getChat().getFirstName());
                    break;
                default:
                    sendMessage(String.valueOf(update.getMessage().getChatId()),
                            "Unknown command");
            }
        }
    }

    private void startCommandReceived(String chatId, String name) {
        String answer = String.format("Hi %s nice to meet you!", name);
        sendMessage(chatId, answer);
    }

    @Override
    public void sendMessage(String chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Can`t send message due to error occurred: ", e);
        }
    }

    @Override
    public void sendMessageAboutNewRental(RentalResponseDto rental) {
        User userById = userService.findById(rental.getUserId());
        if (userById.getChatId() != null) {
            sendMessage(userById.getChatId(),
                    "New rental was added with ID: "
                            + rental.getId() + "\n"
                            + "Car brand:" + carService.findById(rental.getCarId())
                            .getBrand() + "\n"
                            + "Rental date: " + rental.getRentalDate() + "\n"
                            + "Return date: " + rental.getReturnDate());
        }
    }

    private boolean matchPattern(String email) {
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Scheduled(cron = "0 9 * * *") // Every day at 9 p.m
    public void notifyAllUsersWhereActualReturnDateIsAfterReturnDate() {
        List<Rental> rentals = rentalService.findAllByActualReturnDateAfterReturnDate();
        int rentalQuantity = rentals.size();
        if (rentalQuantity == 0) {
            sendMessage(botConfig.getAdminId(),
                    "No rentals overdue today!");
        } else {
            sendMessage(botConfig.getAdminId(),
                    String.format("Today %s rentals overdue!", rentalQuantity));
        }
        for (Rental rental : rentals) {
            sendMessage(userService.findById(rental.getUser().getId()).getChatId(),
                    "Your car has to be returned, because your rental ended");
        }
    }
}
