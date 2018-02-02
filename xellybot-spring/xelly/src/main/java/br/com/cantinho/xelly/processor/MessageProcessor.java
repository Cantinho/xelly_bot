package br.com.cantinho.xelly.processor;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageProcessor implements Processor {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  public MessageProcessor() {
    //empty constructor
  }


  @Override
  @Subscribe
  public void onIncomingEvent(MessageEvent messageEvent) {
    log.info("onIncomingEvent:" + messageEvent);
  }

}
