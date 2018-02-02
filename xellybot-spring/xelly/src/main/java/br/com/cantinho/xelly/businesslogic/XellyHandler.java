package br.com.cantinho.xelly.businesslogic;

import br.com.cantinho.xelly.config.BotConfig;
import br.com.cantinho.xelly.processor.IncomingEventListener;
import br.com.cantinho.xelly.processor.MessageEvent;
import br.com.cantinho.xelly.processor.OutcomingEventListener;
import br.com.cantinho.xelly.processor.Processor;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class XellyHandler extends TelegramLongPollingBot implements OutcomingEventListener {

  public static final EventBus eventbusIn = new EventBus();
  public static final EventBus eventbusOut = new EventBus();

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  public XellyHandler() {
    //empty constructor
  }
  public XellyHandler(final Processor... processors) {
    for(final Processor processor : processors) {
      eventbusIn.register(processor);
    }
    eventbusOut.register(this);
  }

  public void register(final IncomingEventListener incomingEventListener) {
    if(incomingEventListener != null) {
      eventbusIn.register(incomingEventListener);
    }
  }

  public void unRegister(final IncomingEventListener incomingEventListener) {
    if(incomingEventListener != null) {
      eventbusIn.unregister(incomingEventListener);
    }
  }

  public void register(final OutcomingEventListener outcomingEventListener) {
    if(outcomingEventListener != null) {
      eventbusOut.register(outcomingEventListener);
    }
  }

  public void unRegister(final OutcomingEventListener outcomingEventListener) {
    if(outcomingEventListener != null) {
      eventbusOut.unregister(outcomingEventListener);
    }
  }

  @Override
  public void onUpdateReceived(Update update) {
    log.info("onUpdateReceived");

    // check if the update has a message
    if(update.hasMessage()) {
      final Message message = update.getMessage();

      // check if the message has text. It could also contain for example a location (message
      // .hasLocation()
      if(message.hasText()) {
        eventbusIn.post(new MessageEvent(message.getText()));
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
  @Subscribe
  public void onOutcomingEvent(final MessageEvent messageEvent) {
    log.info("onOutcomingEvent: " + messageEvent);
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
