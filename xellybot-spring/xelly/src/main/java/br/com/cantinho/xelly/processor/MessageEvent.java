package br.com.cantinho.xelly.processor;

/**
 * A text event.
 */
public class MessageEvent {

  /**
   * A text.
   */
  private final String text;

  /**
   * Builds an event with a string text.
   *
   * @param text The text.
   */
  public MessageEvent(final String text) {
    this.text = text;
  }

  /**
   * Retrieves a text.
   *
   * @return
   */
  public String getText() {
    return text;
  }

  @Override
  public String toString() {
    return "MessageEvent{" +
        "text='" + text + '\'' +
        '}';
  }
}
