package br.com.cantinho.xelly;

import br.com.cantinho.xelly.businesslogic.XellyHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@SpringBootApplication
public class XellyApplication {

//	public static void main(String[] args) {
//		SpringApplication.run(XellyApplication.class, args);
//	}

  public static void main(String[] args) {
    ApiContextInitializer.init();
    TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
    try {
      telegramBotsApi.registerBot(new XellyHandler());
//      telegramBotsApi.registerBot(new DirectionsHandlers());
//      telegramBotsApi.registerBot(new RaeHandlers());
//      telegramBotsApi.registerBot(new WeatherHandlers());
//      telegramBotsApi.registerBot(new TransifexHandlers());
//      telegramBotsApi.registerBot(new FilesHandlers());
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }
}
