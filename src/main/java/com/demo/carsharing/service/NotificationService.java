package com.demo.carsharing.service;

import com.demo.carsharing.dto.response.RentalResponseDto;

public interface NotificationService {
    void sendMessage(String chatId, String textToSend);

    void sendMessageAboutNewRental(RentalResponseDto rental);
}
