package br.com.cantinho.xelly.businesslogic;

import br.com.cantinho.xelly.config.BotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class XellyHandler extends TelegramLongPollingBot {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Override
  public void onUpdateReceived(Update update) {
    log.info("onUpdateReceived");

    // check if the update has a message
    if(update.hasMessage()) {
      final Message message = update.getMessage();

      // check if the message has text. It could also contain for example a location (message
      // .hasLocation()
      if(message.hasText()) {
        // create a object that contains the information to send back the message
        SendMessage sendRequest = new SendMessage();
        sendRequest.setChatId(message.getChatId().toString()); // who should get the message? The
        // sender from which we got the message...
        sendRequest.setText("you said: " + message.getText());
        try {
          sendApiMethod(sendRequest);
        } catch (final TelegramApiException exc) {
          //TODO some error handling
        }
      }
    }
  }

  @Override
  public String getBotUsername() {
    log.info("getBotUsername");
    return BotConfig.USERNAMEPROJECT;
  }

  @Override
  public String getBotToken() {
    log.info("getBotToken");
    return BotConfig.TOKENMYPROJECT;
  }
}
