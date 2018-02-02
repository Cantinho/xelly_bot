package br.com.cantinho.xelly.processor;

import com.google.common.eventbus.Subscribe;

public interface OutcomingEventListener {

  /**
   * When message events are going out.
   * @param messageEvent
   */
  @Subscribe
  void onOutcomingEvent(final MessageEvent messageEvent);
}
