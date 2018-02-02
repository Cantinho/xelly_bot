package br.com.cantinho.xelly.processor;

import com.google.common.eventbus.Subscribe;

public interface IncomingEventListener {

  /**
   * When message events are arriving.
   * @param messageEvent
   */
  @Subscribe
  void onIncomingEvent(final MessageEvent messageEvent);
}
