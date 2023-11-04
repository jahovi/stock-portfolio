package webSocket;

/**
 * This interface specifies capabilities a class needs to have in order to be registered with a web socket client
 * to be informed of and process incoming trade messages from the data provider. 
 */
public interface IWebSocketListener {
    
    /**
     * Process trade messages received from the data provider through the web socket connection. 
     *
     * @param message the trade message 
     */
    void processWebSocketTradeMessage(String message);
}
