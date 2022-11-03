package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MyDeportBot extends TelegramLongPollingBot {

    MainController mainController;

    public MyDeportBot() {
        this.mainController = new MainController(this);
    }

    @Override
    public String getBotUsername() {
        return " @my_test_jon_bot";
    }

    @Override
    public String getBotToken() {
        return "1025284982:AAFna_8GAJK93QtMrQId22-LFBV5Rk1hGJY";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage())
        {
            Message message = update.getMessage();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
          //  sendMessage.setText("has message");
         //   send(sendMessage);
            if(message.hasText())
            {
                mainController.handleMessage(message);
            }else {

                sendMessage.setText("wrong meessage");
                send(sendMessage);

            }

        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            try {
                mainController.handleCallbackQuery(callbackQuery);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


    }


    public void send(SendMessage sendMessage)
    {
        try {
           execute(sendMessage);
        }catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }
}
