package com.demo.carsharing.service.impl;

import com.demo.carsharing.config.BotConfig;
import com.demo.carsharing.dto.response.RentalResponseDto;
import com.demo.carsharing.dto.response.UserResponseDto;
import com.demo.carsharing.model.User;
import com.demo.carsharing.repository.UserRepository;
import com.demo.carsharing.service.CarService;
import com.demo.carsharing.service.NotificationService;
import com.demo.carsharing.service.RentalService;
import com.demo.carsharing.service.UserService;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
@AllArgsConstructor
public class TelegramNotificationServiceImpl extends TelegramLongPollingBot
        implements NotificationService {

    private static final String FIRST_MESSAGE = "/start";
    private final BotConfig botConfig;
    private final UserRepository userRepository;
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
            String chatId = String.valueOf(update.getMessage().getChatId());
            if (messageText.equals(FIRST_MESSAGE)) {
                greetMessage(chatId, update.getMessage().getChat().getFirstName());
            } else {
                Optional<User> userByEmail = userRepository.findByEmail(messageText);
                if (userByEmail.isPresent()) {
                    User user = userByEmail.get();
                    user.setChatId(chatId);
                    userRepository.save(user);
                    thankYouMessage(chatId);
                } else {
                    failMessage(chatId);
                }
            }
        }
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
        UserResponseDto userById = userService.findById(rental.getUser().getId());
        if (userById.getChatId() != null) {
            sendMessage(userById.getChatId(),
                    "New rental was added with ID: "
                            + rental.getId() + "\n"
                            + "Car brand:"
                            + carService.findById(rental.getCar().getId()).getBrand() + "\n"
                            + "Rental date: " + rental.getRentalDate() + "\n"
                            + "Return date: " + rental.getReturnDate());

        }
    }

    @Scheduled(cron = "0 9 * * *") //  Every day at 9 p.m
    public void notifyAllUsersWhereActualReturnDateIsAfterReturnDate() {
        List<RentalResponseDto> rentals = rentalService.findAllByActualReturnDateAfterReturnDate();
        int rentalQuantity = rentals.size();
        if (rentalQuantity == 0) {
            sendMessage(botConfig.getAdminId(),
                    "No rentals overdue today!");
            log.info("Cron job send message to admin");
        } else {
            sendMessage(botConfig.getAdminId(),
                    String.format("Today %s rentals overdue!", rentalQuantity));
            log.info("Cron job send message to admin");
        }
        for (RentalResponseDto rental : rentals) {
            sendMessage(userService.findById(rental.getUser().getId()).getChatId(),
                    "Your car has to be returned, because your rental ended");
            log.info("Cron job send message to user"
                    + userService.findById(rental.getUser().getId()));
        }
    }

    private void greetMessage(String chatId, String name) {
        String answer = String.format("Hello %s: . \n Please send your email: ", name);
        sendMessage(chatId, answer);
    }

    private void failMessage(String chatId) {
        String text = "User with this email doesn't exist in DB, please check your credential";
        sendMessage(chatId, text);
    }

    private void thankYouMessage(String chatId) {
        String text = "You are successfully sync with your account.";
        sendMessage(chatId, text);
    }
}
